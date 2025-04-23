package lot.database;

import lot.database.triggers.ManageSeatAvailabilityAfterUpdateTrigger;
import lot.database.triggers.MakeSeatAvailableTrigger;
import lot.database.triggers.MakeSeatUnavailableTrigger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.h2.api.Trigger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class TriggersTest {
    @BeforeAll
    static void setup() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        DataSource inMemoryDataSource = new HikariDataSource(config);

        DatabaseInitializer.overrideDataSource(inMemoryDataSource);
        DatabaseInitializer.initialize();
    }


    @Test
    void testFireOnInsert() throws SQLException {
        try (Connection conn = DatabaseInitializer.getConnection()) {
            int flightId = insertTestFlight(conn);
            int passengerId = insertTestPassenger(conn);
            insertTestSeat(conn, flightId, "1A", true);

            Trigger trigger = new MakeSeatUnavailableTrigger();
            trigger.init(conn, null, null, "reservations", false, Trigger.INSERT);

            Object[] newRow = new Object[]{null, flightId, passengerId, "1A"};

            assert(isSeatAvailable(conn, flightId, "1A"));

            trigger.fire(conn, null, newRow);

            assertFalse(isSeatAvailable(conn, flightId, "1A"));

            conn.createStatement().execute("DELETE FROM seats WHERE flightId = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
        }
    }


    @Test
    void testFireOnDelete() throws SQLException {
        try (Connection conn = DatabaseInitializer.getConnection()) {
            int flightId = insertTestFlight(conn);
            int passengerId = insertTestPassenger(conn);
            insertTestSeat(conn, flightId, "2B", false);

            Trigger trigger = new MakeSeatAvailableTrigger();
            trigger.init(conn, null, null, "reservations", false, Trigger.DELETE);

            Object[] oldRow = new Object[]{1, flightId, passengerId, "2B"};

            assertFalse(isSeatAvailable(conn, flightId, "2B"));

            trigger.fire(conn, oldRow, null);

            assertTrue(isSeatAvailable(conn, flightId, "2B"));

            conn.createStatement().execute("DELETE FROM seats WHERE flightId = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
        }
    }


    @Test
    void testFireOnUpdateWithSeatChange() throws SQLException {
        try (Connection conn = DatabaseInitializer.getConnection()) {
            int flightId = insertTestFlight(conn);
            int passengerId = insertTestPassenger(conn);
            insertTestSeat(conn, flightId, "3C", false);
            insertTestSeat(conn, flightId, "4D", true);

            Trigger trigger = new ManageSeatAvailabilityAfterUpdateTrigger();
            trigger.init(conn, null, null, "reservations", false, Trigger.UPDATE);

            Object[] oldRow = new Object[]{1, flightId, passengerId, "3C"};

            Object[] newRow = new Object[]{1, flightId, passengerId, "4D"};

            assertFalse(isSeatAvailable(conn, flightId, "3C"));
            assertTrue(isSeatAvailable(conn, flightId, "4D"));

            trigger.fire(conn, oldRow, newRow);

            assertTrue(isSeatAvailable(conn, flightId, "3C"));
            assertFalse(isSeatAvailable(conn, flightId, "4D"));

            conn.createStatement().execute("DELETE FROM seats WHERE flightId = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
        }
    }

    @Test
    void testFireOnUpdateWithoutSeatChange() throws SQLException {
        try (Connection conn = DatabaseInitializer.getConnection()) {
            int flightId = insertTestFlight(conn);
            int passengerId = insertTestPassenger(conn);
            insertTestSeat(conn, flightId, "5E", false);

            Trigger trigger = new ManageSeatAvailabilityAfterUpdateTrigger();
            trigger.init(conn, null, null, "reservations", false, Trigger.UPDATE);

            Object[] oldRow = new Object[]{1, flightId, passengerId, "5E"};

            Object[] newRow = new Object[]{1, flightId, passengerId + 1, "5E"};

            assertFalse(isSeatAvailable(conn, flightId, "5E"));

            trigger.fire(conn, oldRow, newRow);

            assertFalse(isSeatAvailable(conn, flightId, "5E"));

            conn.createStatement().execute("DELETE FROM seats WHERE flightId = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
        }
    }

    private int insertTestFlight(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                INSERT INTO flights (departure, destination, departureDate, duration, seatRowsAmount)
                VALUES ('Warsaw', 'London', CURRENT_TIMESTAMP + INTERVAL '1' DAY, 120, 10)
            """, Statement.RETURN_GENERATED_KEYS);

            var rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
    }

    private int insertTestPassenger(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                INSERT INTO passengers (name, surname, email, phoneNumber)
                VALUES ('John', 'Doe', 'john.doe@example.com', '123456789')
            """, Statement.RETURN_GENERATED_KEYS);

            var rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
    }

    private void insertTestSeat(Connection conn, int flightId, String seatNumber, boolean available) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("""
            INSERT INTO seats (flightId, seatNumber, available)
            VALUES (?, ?, ?)
        """)) {
            ps.setInt(1, flightId);
            ps.setString(2, seatNumber);
            ps.setBoolean(3, available);
            ps.executeUpdate();
        }
    }

    private boolean isSeatAvailable(Connection conn, int flightId, String seatNumber) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("""
            SELECT available FROM seats WHERE flightId = ? AND seatNumber = ?
        """)) {
            ps.setInt(1, flightId);
            ps.setString(2, seatNumber);
            var rs = ps.executeQuery();
            return rs.next() && rs.getBoolean(1);
        }
    }
}
