package lot.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Flight;
import lot.models.Passenger;
import lot.models.Reservation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDaoTest {
    private ReservationDao reservationDao;
    private FlightDao flightDao;
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
        reservationDao = new ReservationDao();
        flightDao = new FlightDao();
        passengerDao = new PassengerDao();
    }

    @Test
    void testSaveAndFindById() throws DatabaseActionException, SQLException {
        Flight flight = createTestFlight();
        Passenger passenger = createTestPassenger();

        int flightId = flightDao.save(flight);
        int passengerId = passengerDao.save(passenger);

        Reservation reservation = new Reservation(flightId, passengerId, "1A");

        int id = reservationDao.save(reservation);
        Reservation foundReservation = reservationDao.findById(id);

        assertNotNull(foundReservation);
        assertEquals(flightId, foundReservation.getFlightId());
        assertEquals(passengerId, foundReservation.getPassengerId());
        assertEquals("1A", foundReservation.getSeatNumber());
        assertFalse(foundReservation.getTookPlace());

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM reservations WHERE id = " + id);
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
        }
    }

    @Test
    void testFindAllByForeignKey() throws DatabaseActionException, SQLException {
        Flight flight = createTestFlight();
        Passenger passenger1 = createTestPassenger();
        Passenger passenger2 = createTestPassenger();

        int flightId = flightDao.save(flight);
        int passengerId1 = passengerDao.save(passenger1);
        int passengerId2 = passengerDao.save(passenger2);

        Reservation reservation1 = new Reservation(flightId, passengerId1, "2A");
        Reservation reservation2 = new Reservation(flightId, passengerId2, "2B");

        int id1 = reservationDao.save(reservation1);
        int id2 = reservationDao.save(reservation2);

        List<Reservation> flightReservations = reservationDao.findAllByForeignKey("flights", flightId);
        List<Reservation> passengerReservations = reservationDao.findAllByForeignKey("passengers", passengerId1);

        assertTrue(flightReservations.size() >= 2);
        assertTrue(flightReservations.stream().allMatch(r -> r.getFlightId() == flightId));

        assertFalse(passengerReservations.isEmpty());
        assertTrue(passengerReservations.stream().allMatch(r -> r.getPassengerId() == passengerId1));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM reservations WHERE id IN (" + id1 + "," + id2 + ")");
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id IN (" + passengerId1 + "," + passengerId2 + ")");
        }
    }

    @Test
    void testFindAllBySurname() throws DatabaseActionException, SQLException {
        Flight flight = createTestFlight();
        Passenger passenger1 = new Passenger("John", "Smith", "john@example.com", "111222333");
        Passenger passenger2 = new Passenger("Jane", "Smith", "jane@example.com", "444555666");

        int flightId = flightDao.save(flight);
        int passengerId1 = passengerDao.save(passenger1);
        int passengerId2 = passengerDao.save(passenger2);

        Reservation reservation1 = new Reservation(flightId, passengerId1, "3A");
        Reservation reservation2 = new Reservation(flightId, passengerId2, "3B");

        int id1 = reservationDao.save(reservation1);
        int id2 = reservationDao.save(reservation2);

        List<Reservation> smithReservations = reservationDao.findAllBySurname("Smith");

        assertTrue(smithReservations.size() >= 2);
        assertTrue(smithReservations.stream().allMatch(r ->
                r.getPassengerSurname().equals("Smith")));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM reservations WHERE id IN (" + id1 + "," + id2 + ")");
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id IN (" + passengerId1 + "," + passengerId2 + ")");
        }
    }

    @Test
    void testHasTakenPlace() throws DatabaseActionException, SQLException {
        Flight pastFlight = new Flight("Past", "Flight", LocalDateTime.now().minusDays(1), 60, 5);
        Flight futureFlight = new Flight("Future", "Flight", LocalDateTime.now().plusDays(1), 60, 5);

        int pastFlightId = flightDao.save(pastFlight);
        int futureFlightId = flightDao.save(futureFlight);

        assertTrue(reservationDao.hasTakenPlace(pastFlightId));
        assertFalse(reservationDao.hasTakenPlace(futureFlightId));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id IN (" + pastFlightId + "," + futureFlightId + ")");
        }
    }

    @Test
    void testUpdate() throws DatabaseActionException, SQLException {
        Flight flight1 = createTestFlight();
        Flight flight2 = createTestFlight();
        Passenger passenger1 = createTestPassenger();
        Passenger passenger2 = createTestPassenger();

        int flightId1 = flightDao.save(flight1);
        int flightId2 = flightDao.save(flight2);
        int passengerId1 = passengerDao.save(passenger1);
        int passengerId2 = passengerDao.save(passenger2);

        Reservation reservation = new Reservation(flightId1, passengerId1, "4A");
        int id = reservationDao.save(reservation);

        Reservation toUpdateReservation = new Reservation(id, flightId2, passengerId2, "4B");
        reservationDao.update(toUpdateReservation);

        Reservation updatedReservation = reservationDao.findById(id);
        assertEquals(flightId2, updatedReservation.getFlightId());
        assertEquals(passengerId2, updatedReservation.getPassengerId());
        assertEquals("4B", updatedReservation.getSeatNumber());

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM reservations WHERE id = " + id);
            conn.createStatement().execute("DELETE FROM flights WHERE id IN (" + flightId1 + "," + flightId2 + ")");
            conn.createStatement().execute("DELETE FROM passengers WHERE id IN (" + passengerId1 + "," + passengerId2 + ")");
        }
    }

    @Test
    void testDelete() throws DatabaseActionException, SQLException {
        Flight flight = createTestFlight();
        Passenger passenger = createTestPassenger();

        int flightId = flightDao.save(flight);
        int passengerId = passengerDao.save(passenger);

        Reservation reservation = new Reservation(flightId, passengerId, "3A");

        int id = reservationDao.save(reservation);

        reservationDao.delete(id);

        assertThrows(DatabaseActionException.class, () -> reservationDao.findById(id));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
        }
    }

    @Test
    void testFindAll() throws DatabaseActionException, SQLException {
        Flight flight = createTestFlight();
        Passenger passenger = createTestPassenger();

        int flightId = flightDao.save(flight);
        int passengerId = passengerDao.save(passenger);

        Reservation reservation = new Reservation(flightId, passengerId, "5A");
        int id = reservationDao.save(reservation);

        List<Reservation> allReservations = reservationDao.findAll();

        assertFalse(allReservations.isEmpty());
        assertTrue(allReservations.stream().anyMatch(r -> r.getId() == id));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM reservations WHERE id = " + id);
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
        }
    }

    @Test
    void testFindAllId() throws DatabaseActionException, SQLException {
        Flight flight = createTestFlight();
        Passenger passenger = createTestPassenger();

        int flightId = flightDao.save(flight);
        int passengerId = passengerDao.save(passenger);

        Reservation reservation = new Reservation(flightId, passengerId, "2A");
        int id = reservationDao.save(reservation);

        List<Integer> allIds = reservationDao.findAllId();

        assertFalse(allIds.isEmpty());
        assertTrue(allIds.contains(id));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM reservations WHERE id = " + id);
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
        }
    }

    @Test
    void testExistsById() throws DatabaseActionException, SQLException {
        Flight flight = createTestFlight();
        Passenger passenger = createTestPassenger();

        int flightId = flightDao.save(flight);
        int passengerId = passengerDao.save(passenger);

        Reservation reservation = new Reservation(flightId, passengerId, "4A");
        int id = reservationDao.save(reservation);

        assertTrue(reservationDao.existsById(id));
        assertFalse(reservationDao.existsById(9999));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM reservations WHERE id = " + id);
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + flightId);
            conn.createStatement().execute("DELETE FROM passengers WHERE id = " + passengerId);
        }
    }

    @AfterAll
    static void reset() {
        DatabaseInitializer.resetToDefault();
    }

    private Flight createTestFlight() {
        Flight flight = new Flight("Test", "Flight", LocalDateTime.now().plusDays(1), 60, 5);
        return flight;
    }

    private Passenger createTestPassenger() {
        Passenger passenger = new Passenger("Test", "Passenger", "test@example.com", "123123123");
        return passenger;
    }
}