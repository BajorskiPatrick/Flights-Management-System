package lot.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Passenger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassengerDaoTest {
    private PassengerDao passengerDao;
    private static HikariDataSource inMemoryDataSource;

    @BeforeAll
    static void setup() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        inMemoryDataSource = new HikariDataSource(config);

        DatabaseInitializer.overrideDataSource(inMemoryDataSource);
        DatabaseInitializer.initialize();
    }

    @BeforeEach
    void setUp() {
        passengerDao = new PassengerDao();
    }

    @Test
    void testSaveAndFindById() throws DatabaseActionException, SQLException {
        Passenger passenger = new Passenger("John", "Doe", "john.doe@example.com", "123456789");

        int id = passengerDao.save(passenger);
        Passenger foundPassenger = passengerDao.findById(id);

        assertNotNull(foundPassenger);
        assertEquals("John", foundPassenger.getName());
        assertEquals("Doe", foundPassenger.getSurname());
        assertEquals("john.doe@example.com", foundPassenger.getEmail());

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + id);
        }
    }

    @Test
    void testFindBySurname() throws DatabaseActionException, SQLException {
        Passenger passenger1 = new Passenger("Alice", "Smith", "alice@example.com", "111222333");
        Passenger passenger2 = new Passenger("Bob", "Smith", "bob@example.com", "444555666");

        int id1 = passengerDao.save(passenger1);
        int id2 = passengerDao.save(passenger2);

        List<Passenger> smithPassengers = passengerDao.findBySurname("Smith");

        assertTrue(smithPassengers.size() >= 2);
        assertTrue(smithPassengers.stream().allMatch(p -> p.getSurname().equals("Smith")));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM passengers WHERE id IN (" + id1 + "," + id2 + ")");
        }
    }

    @Test
    void testUpdate() throws DatabaseActionException, SQLException {
        Passenger passenger = new Passenger("Eve", "Johnson", "eve@example.com", "987654321");

        int id = passengerDao.save(passenger);

        passenger.setId(id);
        passenger.setEmail("new.email@example.com");
        passengerDao.update(passenger);

        Passenger updatedPassenger = passengerDao.findById(id);
        assertEquals("new.email@example.com", updatedPassenger.getEmail());

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + id);
        }
    }

    @Test
    void testDelete() throws DatabaseActionException, SQLException {
        Passenger passenger = new Passenger("Test", "Delete", "delete@example.com", "000000000");

        int id = passengerDao.save(passenger);

        passengerDao.delete(id);

        assertThrows(DatabaseActionException.class, () -> passengerDao.findById(id));
    }

    @Test
    void testFindAll() throws DatabaseActionException, SQLException {
        Passenger passenger = new Passenger("FindAll", "Test", "findall@example.com", "123123123");
        int id = passengerDao.save(passenger);

        List<Passenger> allPassengers = passengerDao.findAll();

        assertFalse(allPassengers.isEmpty());
        assertTrue(allPassengers.stream().anyMatch(p -> p.getId() == id));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + id);
        }
    }

    @Test
    void testFindAllId() throws DatabaseActionException, SQLException {
        Passenger passenger = new Passenger("FindAllId", "Test", "findallid@example.com", "321321321");
        int id = passengerDao.save(passenger);

        List<Integer> allIds = passengerDao.findAllId();

        assertFalse(allIds.isEmpty());
        assertTrue(allIds.contains(id));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + id);
        }
    }

    @Test
    void testExistsById() throws DatabaseActionException, SQLException {
        Passenger passenger = new Passenger("Exists", "Test", "exists@example.com", "999888777");
        int id = passengerDao.save(passenger);

        assertTrue(passengerDao.existsById(id));
        assertFalse(passengerDao.existsById(9999));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + id);
        }
    }

    @AfterAll
    static void reset() {
        DatabaseInitializer.resetToDefault();
    }
}