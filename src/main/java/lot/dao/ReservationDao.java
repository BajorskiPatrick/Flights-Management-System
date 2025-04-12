package lot.dao;

import lot.config.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Reservation;
import lot.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao implements GenericDao<Reservation> {
    @Override
    public Reservation findById(int id) throws DatabaseActionException {
        String query =
                """
                SELECT *
                FROM reservations r
                WHERE r.id = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return ResultSetMapper.mapReservation(rs);
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching reservation details", e);
        }
    }

    @Override
    public List<Reservation> findAll() throws DatabaseActionException {
        List<Reservation> reservations = new ArrayList<>();

        String query =
                """
                SELECT *
                FROM reservations
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                Statement statement = conn.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                reservations.add(ResultSetMapper.mapReservation(rs));
            }
            return reservations;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching all reservations details", e);
        }
    }

    @Override
    public void save(Reservation reservation) throws DatabaseActionException {
        String query =
                """
                INSERT INTO reservations (flightId, passengerId, seatNumber) VALUES 
                (?, ?, ?)
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, reservation.getFlightId());
            ps.setInt(2, reservation.getPassengerId());
            ps.setString(3, reservation.getSeatNumber());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while saving new reservation", e);
        }
    }

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
            return rs.next();
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while checking if the reservation exists", e);
        }
    }
}
