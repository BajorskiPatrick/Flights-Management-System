package lot.dao;

import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Reservation;
import lot.dao.utils.ResultSetMapper;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object for Reservation entities, providing database operations.
 */
public class ReservationDao implements GenericDao<Reservation> {
    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public ReservationDao() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation findById(int id) throws DatabaseActionException {
        String query =
                """
                SELECT r.id, r.flightId, r.passengerId, p.name, p.surname, r.seatNumber, f.departureDate
                FROM reservations r
                JOIN passengers p ON r.passengerId = p.id
                JOIN flights f ON r.flightId = f.id
                WHERE r.id = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Reservation reservation = ResultSetMapper.mapReservation(rs);
            reservation.setTookPlace(this.hasTakenPlace(reservation.getFlightId()));
            rs.close();
            return reservation;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching reservation details by reservationId", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> findAll() throws DatabaseActionException {
        List<Reservation> reservations = new ArrayList<>();

        String query =
                """
                SELECT r.id, r.flightId, r.passengerId, p.name, p.surname, r.seatNumber, f.departureDate
                FROM reservations r
                JOIN passengers p ON r.passengerId = p.id
                JOIN flights f ON r.flightId = f.id
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                Statement statement = conn.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Reservation reservation = ResultSetMapper.mapReservation(rs);
                reservation.setTookPlace(this.hasTakenPlace(reservation.getFlightId()));
                reservations.add(reservation);
            }
            rs.close();
            return reservations;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching all reservations details", e);
        }
    }

    /**
     * Finds reservations by a foreign key (flight or passenger).
     *
     * @param foreignKeyTable the foreign key table name ("flights" or "passengers")
     * @param foreignKeyId the foreign key ID
     * @return a list of matching reservations
     * @throws DatabaseActionException if a database error occurs or invalid table name is provided
     */
    public List<Reservation> findAllByForeignKey(String foreignKeyTable, int foreignKeyId) throws DatabaseActionException {
        Map<String, String> allowedTables = new HashMap<>();
        allowedTables.put("flights", "flightId");
        allowedTables.put("passengers", "passengerId");

        foreignKeyTable = foreignKeyTable.toLowerCase().trim();

        if (!allowedTables.containsKey(foreignKeyTable)) {
            throw new DatabaseActionException("Column of table " + foreignKeyTable + " does not exist in reservations table as a foreign key");
        }

        List<Reservation> reservations = new ArrayList<>();
        String query =
                """
                SELECT r.id, r.flightId, r.passengerId, p.name, p.surname, r.seatNumber, f.departureDate
                FROM reservations r
                JOIN passengers p ON r.passengerId = p.id
                JOIN flights f ON r.flightId = f.id
                WHERE r.
                """;
        query += allowedTables.get(foreignKeyTable) + " = ?";
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, foreignKeyId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = ResultSetMapper.mapReservation(rs);
                reservation.setTookPlace(this.hasTakenPlace(reservation.getFlightId()));
                reservations.add(reservation);
            }
            rs.close();
            return reservations;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching reservations details by " + foreignKeyTable + "Id", e);
        }
    }

    /**
     * Finds reservations by passenger surname.
     *
     * @param surname the passenger surname to search for
     * @return a list of matching reservations
     * @throws DatabaseActionException if a database error occurs
     */
    public List<Reservation> findAllBySurname(String surname) throws DatabaseActionException {
        List<Reservation> reservations = new ArrayList<>();
        String query =
                """
                SELECT r.id, r.flightId, r.passengerId, p.name, p.surname, r.seatNumber, f.departureDate
                FROM reservations r
                JOIN passengers p ON r.passengerId = p.id
                JOIN flights f ON r.flightId = f.id
                WHERE p.surname = ?
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, surname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = ResultSetMapper.mapReservation(rs);
                reservation.setTookPlace(this.hasTakenPlace(reservation.getFlightId()));
                reservations.add(reservation);
            }
            rs.close();
            return reservations;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching reservations details by surname: " + surname, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int save(Reservation reservation) throws DatabaseActionException {
        String query =
                """
                INSERT INTO reservations (flightId, passengerId, seatNumber) VALUES
                (?, ?, ?)
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, reservation.getFlightId());
            ps.setInt(2, reservation.getPassengerId());
            ps.setString(3, reservation.getSeatNumber());

            ps.executeUpdate();

            int newId;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                } else {
                    throw new DatabaseActionException("No generated ID received after saving new reservation");
                }
            }
            return newId;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while saving new reservation", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Reservation reservation) throws DatabaseActionException {
        String query =
                """
                UPDATE reservations
                SET flightId = ?, passengerId = ?, seatNumber = ?
                WHERE id = ?
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, reservation.getFlightId());
            ps.setInt(2, reservation.getPassengerId());
            ps.setString(3, reservation.getSeatNumber());
            ps.setInt(4, reservation.getId());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while updating reservation details", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(int id) throws DatabaseActionException {
        String query =
                """
                DELETE FROM reservations
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
            throw new DatabaseActionException("Database error while deleting reservation details", e);
        }
    }

    /**
     * Checks if a flight associated with a reservation has already taken place.
     *
     * @param flightId the ID of the flight to check
     * @return true if the flight has taken place, false otherwise
     * @throws DatabaseActionException if a database error occurs
     */
    public Boolean hasTakenPlace(int flightId) throws DatabaseActionException {
        String query =
                """
                SELECT 1
                FROM flights f
                WHERE f.id = ? AND f.departureDate > CURRENT_TIMESTAMP
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();
            Boolean res = !rs.next();
            rs.close();
            return res;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while checking if flight assigned to reservation has already taken place", e);
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
                FROM reservations
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
                FROM reservations r
                WHERE r.id = ?
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
            throw new DatabaseActionException("Database error while checking if the reservation exists", e);
        }
    }
}
