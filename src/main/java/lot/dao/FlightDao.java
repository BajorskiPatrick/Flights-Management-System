package lot.dao;

import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Flight;
import lot.dao.utils.ResultSetMapper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Flight entities, providing database operations.
 */
public class FlightDao implements GenericDao<Flight> {
    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public FlightDao() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Flight findById(int id) throws DatabaseActionException {
        String query =
                """
                SELECT *
                FROM flights f
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Flight flight = ResultSetMapper.mapFlight(rs);
            rs.close();
            return flight;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching flight details by id", e);
        }
    }

    /**
     * Finds flights by departure location.
     *
     * @param departure the departure location to search for
     * @return a list of flights matching the departure location
     * @throws DatabaseActionException if a database error occurs
     */
    public List<Flight> findByDeparture(String departure) throws DatabaseActionException {
        String query =
                """
                SELECT *
                FROM flights f
                WHERE departure = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, departure);
            ResultSet rs = ps.executeQuery();
            List<Flight> flights = new ArrayList<>();
            while (rs.next()) {
                Flight flight = ResultSetMapper.mapFlight(rs);
                flights.add(flight);
            }
            rs.close();
            return flights;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching flight details by departure", e);
        }
    }

    /**
     * Finds flights by destination location.
     *
     * @param destination the destination location to search for
     * @return a list of flights matching the destination location
     * @throws DatabaseActionException if a database error occurs
     */
    public List<Flight> findByDestination(String destination) throws DatabaseActionException {
        String query =
                """
                SELECT *
                FROM flights f
                WHERE destination = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, destination);
            ResultSet rs = ps.executeQuery();
            List<Flight> flights = new ArrayList<>();
            while (rs.next()) {
                Flight flight = ResultSetMapper.mapFlight(rs);
                flights.add(flight);
            }
            rs.close();
            return flights;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching flight details by destination", e);
        }
    }

    /**
     * Finds flights by departure date.
     *
     * @param date the departure date to search for
     * @return a list of flights matching the departure date
     * @throws DatabaseActionException if a database error occurs
     */
    public List<Flight> findByDate(LocalDate date) throws DatabaseActionException {
        String query =
                """
                SELECT *
                FROM flights f
                WHERE CAST(departureDate AS DATE) = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            List<Flight> flights = new ArrayList<>();
            while (rs.next()) {
                Flight flight = ResultSetMapper.mapFlight(rs);
                flights.add(flight);
            }
            rs.close();
            return flights;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching flight details by destination", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Flight> findAll() throws DatabaseActionException {
        List<Flight> flights = new ArrayList<>();

        String query =
                """
                SELECT *
                FROM flights
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                Statement statement = conn.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                flights.add(ResultSetMapper.mapFlight(rs));
            }
            rs.close();
            return flights;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching all flights details", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int save(Flight flight) throws DatabaseActionException {
        String query =
                """
                INSERT INTO flights (departure, destination, departureDate, duration, seatRowsAmount) VALUES
                (?, ?, ?, ?, ?)
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, flight.getDeparture());
            ps.setString(2, flight.getDestination());
            ps.setTimestamp(3, Timestamp.valueOf(flight.getDepartureDate()));
            ps.setInt(4, flight.getDuration());
            ps.setInt(5, flight.getSeatRowsAmount());

            ps.executeUpdate();
            int newId;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                } else {
                    throw new DatabaseActionException("No generated ID received after saving new flight");
                }
            }
            createSeats(conn, newId, 1, flight.getSeatRowsAmount());
            return newId;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while saving new flight", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Flight flight) throws DatabaseActionException {
        String query =
                """
                UPDATE flights
                SET departure = ?, destination = ?, departureDate = ?, duration = ?, seatRowsAmount = ?
                WHERE id = ?
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, flight.getDeparture());
            ps.setString(2, flight.getDestination());
            ps.setTimestamp(3, Timestamp.valueOf(flight.getDepartureDate()));
            ps.setInt(4, flight.getDuration());
            ps.setInt(5, flight.getSeatRowsAmount());
            ps.setInt(6, flight.getId());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while updating flight details", e);
        }
    }

    /**
     * Updates a flight and handles seat adjustments.
     *
     * @param flight the flight to update
     * @param previousSeatRowsAmount the previous number of seat rows
     * @throws DatabaseActionException if a database error occurs
     */
    public void update(Flight flight, int previousSeatRowsAmount) throws DatabaseActionException {
        String query =
                """
                UPDATE flights
                SET departure = ?, destination = ?, departureDate = ?, duration = ?, seatRowsAmount = ?
                WHERE id = ?
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, flight.getDeparture());
            ps.setString(2, flight.getDestination());
            ps.setTimestamp(3, Timestamp.valueOf(flight.getDepartureDate()));
            ps.setInt(4, flight.getDuration());
            ps.setInt(5, flight.getSeatRowsAmount());;
            ps.setInt(6, flight.getId());

            ps.executeUpdate();

            createSeats(conn, flight.getId(), previousSeatRowsAmount + 1, flight.getSeatRowsAmount());
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while updating flight details", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(int id) throws DatabaseActionException {
        String query =
                """
                DELETE FROM flights
                WHERE id = ?
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while deleting flight", e);
        }
    }

    /**
     * Retrieves available seat numbers for a flight.
     *
     * @param flightId the ID of the flight
     * @return a list of available seat numbers
     * @throws DatabaseActionException if a database error occurs
     */
    public List<String> getAvailableSeatsNumbers(int flightId) throws DatabaseActionException {
        String query =
                """
                SELECT s.seatNumber
                FROM seats s
                WHERE s.flightId = ? AND s.available = true
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();
            List<String> res = new ArrayList<>();
            while (rs.next()) {
                res.add(rs.getString(1));
            }
            return res;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching available seats", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findAllId() throws DatabaseActionException {
        List<Integer> ids = new ArrayList<>();

        String query =
                """
                SELECT id
                FROM flights
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                Statement statement = conn.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
            rs.close();
            return ids;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching all flights details", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean existsById(int id) throws DatabaseActionException {
        String query =
                """
                SELECT 1
                FROM flights f
                WHERE f.id = ?
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Boolean res = rs.next();
            rs.close();
            return res;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while checking if flight exists", e);
        }
    }

    /**
     * Creates seats for a flight.
     *
     * @param conn the database connection
     * @param flightId the ID of the flight
     * @param initialNumber the initial seat row number
     * @param seatRowsAmount the total number of seat rows
     * @throws DatabaseActionException if a database error occurs
     */
    private void createSeats(Connection conn, int flightId, int initialNumber, int seatRowsAmount) throws DatabaseActionException {
        String query =
                """
                INSERT INTO seats (flightId, seatNumber, available) VALUES
                (?, ?, ?)
                """;
        String[] letters = new String[] {"A", "B", "C", "D", "E", "F"};

        try {
            for (int i = initialNumber; i <= seatRowsAmount; i++) {
                for (int j = 0; j < 6; j++) {
                    try (PreparedStatement ps = conn.prepareStatement(query)) {
                        ps.setInt(1, flightId);
                        ps.setString(2, i + letters[j]);
                        ps.setBoolean(3, true);
                        ps.executeUpdate();
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while creating seats", e);
        }
    }
}
