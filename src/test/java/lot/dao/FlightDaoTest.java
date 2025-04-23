package lot.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Flight;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightDaoTest {
    private FlightDao flightDao;
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
        flightDao = new FlightDao();
    }

    @Test
    void testSaveAndFindById() throws DatabaseActionException, SQLException {
        Flight flight = new Flight("Warsaw", "London", LocalDateTime.now().plusDays(1), 120, 10);

        int id = flightDao.save(flight);
        Flight foundFlight = flightDao.findById(id);

        assertNotNull(foundFlight);
        assertEquals("Warsaw", foundFlight.getDeparture());
        assertEquals("London", foundFlight.getDestination());
        assertEquals(10, foundFlight.getSeatRowsAmount());

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + id);
        }
    }

    @Test
    void testUpdate() throws DatabaseActionException, SQLException {
        Flight flight = new Flight("Berlin", "Paris", LocalDateTime.now().plusDays(2), 90, 8);

        int id = flightDao.save(flight);

        flight.setId(id);
        flight.setDestination("Rome");
        flightDao.update(flight);

        Flight updatedFlight = flightDao.findById(id);
        assertEquals("Rome", updatedFlight.getDestination());

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + id);
        }
    }

    @Test
    void testUpdateWithSeatAdjustment() throws DatabaseActionException, SQLException {
        Flight flight = new Flight("Berlin", "Paris", LocalDateTime.now().plusDays(2), 90, 8);
        int id = flightDao.save(flight);

        flight.setId(id);
        flight.setSeatRowsAmount(10);
        flightDao.update(flight, 8);

        Flight updatedFlight = flightDao.findById(id);
        assertEquals(10, updatedFlight.getSeatRowsAmount());

        List<String> availableSeats = flightDao.getAvailableSeatsNumbers(updatedFlight.getId());
        assertEquals(60, availableSeats.size());
        assertTrue(availableSeats.contains("9A"));
        assertTrue(availableSeats.contains("10F"));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + id);
        }
    }

    @Test
    void testFindByDeparture() throws DatabaseActionException, SQLException {
        Flight flight1 = new Flight("Madrid", "Barcelona", LocalDateTime.now().plusDays(3), 60, 5);
        Flight flight2 = new Flight("Madrid", "Lisbon", LocalDateTime.now().plusDays(4), 90, 7);

        int id1 = flightDao.save(flight1);
        int id2 = flightDao.save(flight2);

        List<Flight> madridFlights = flightDao.findByDeparture("Madrid");

        assertTrue(madridFlights.size() >= 2);
        assertTrue(madridFlights.stream().allMatch(f -> f.getDeparture().equals("Madrid")));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id IN (" + id1 + "," + id2 + ")");
        }
    }

    @Test
    void testFindByDestination() throws DatabaseActionException, SQLException {
        Flight flight1 = new Flight("Berlin", "Paris", LocalDateTime.now().plusDays(1), 90, 5);
        Flight flight2 = new Flight("London", "Paris", LocalDateTime.now().plusDays(2), 120, 7);

        int id1 = flightDao.save(flight1);
        int id2 = flightDao.save(flight2);

        List<Flight> parisFlights = flightDao.findByDestination("Paris");

        assertTrue(parisFlights.size() >= 2);
        assertTrue(parisFlights.stream().allMatch(f -> f.getDestination().equals("Paris")));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id IN (" + id1 + "," + id2 + ")");
        }
    }

    @Test
    void testFindByDate() throws DatabaseActionException, SQLException {
        LocalDate testDate = LocalDate.now().plusDays(1);
        Flight flight1 = new Flight("Warsaw", "London", testDate.atTime(10, 0), 120, 5);
        Flight flight2 = new Flight("Berlin", "Paris", testDate.atTime(15, 30), 90, 7);

        int id1 = flightDao.save(flight1);
        int id2 = flightDao.save(flight2);

        List<Flight> flightsByDate = flightDao.findByDate(testDate);

        assertTrue(flightsByDate.size() >= 2);
        assertTrue(flightsByDate.stream().allMatch(f ->
                f.getDepartureDate().toLocalDate().equals(testDate)));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id IN (" + id1 + "," + id2 + ")");
        }
    }

    @Test
    void testFindAll() throws DatabaseActionException, SQLException {
        Flight flight = new Flight("Test", "FindAll", LocalDateTime.now().plusDays(1), 60, 5);
        int id = flightDao.save(flight);

        List<Flight> allFlights = flightDao.findAll();

        assertFalse(allFlights.isEmpty());
        assertTrue(allFlights.stream().anyMatch(f -> f.getId() == id));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + id);
        }
    }

    @Test
    void testDelete() throws DatabaseActionException, SQLException {
        Flight flight = new Flight("Prague", "Vienna", LocalDateTime.now().plusDays(5), 75, 6);

        int id = flightDao.save(flight);

        flightDao.delete(id);
        assertThrows(DatabaseActionException.class, () -> flightDao.findById(id));
    }

    @Test
    void testGetAvailableSeatsNumbers() throws DatabaseActionException, SQLException {
        Flight flight = new Flight("SeatTest", "Destination", LocalDateTime.now().plusDays(1), 60, 2);
        int id = flightDao.save(flight);

        List<String> availableSeats = flightDao.getAvailableSeatsNumbers(id);

        assertEquals(12, availableSeats.size());
        assertTrue(availableSeats.contains("1A"));
        assertTrue(availableSeats.contains("2F"));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + id);
        }
    }

    @Test
    void testFindAllId() throws DatabaseActionException, SQLException {
        Flight flight = new Flight("Test", "FindAllId", LocalDateTime.now().plusDays(1), 60, 5);
        int id = flightDao.save(flight);

        List<Integer> allIds = flightDao.findAllId();

        assertFalse(allIds.isEmpty());
        assertTrue(allIds.contains(id));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + id);
        }
    }

    @Test
    void testExistsById() throws DatabaseActionException, SQLException {
        Flight flight = new Flight("Exists", "Test", LocalDateTime.now().plusDays(1), 60, 5);
        int id = flightDao.save(flight);

        assertTrue(flightDao.existsById(id));
        assertFalse(flightDao.existsById(9999));

        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.createStatement().execute("DELETE FROM flights WHERE id = " + id);
        }
    }

    @AfterAll
    static void reset() {
        DatabaseInitializer.resetToDefault();
    }
}