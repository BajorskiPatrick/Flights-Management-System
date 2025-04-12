package lot.dao;

import lot.config.DatabaseInitializer;
import lot.models.Flight;
import lot.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDao implements GenericDao<Flight> {
    public Boolean hasTakenPlace(int flightId) {
        String query =
                """
                SELECT 1
                FROM flights f
                WHERE f.id = ? AND f.departureDate > CURRENT_TIMESTAMP
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
        ) {
            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Flight findById(int id) {
        String query =
                """
                SELECT *
                FROM flights f
                WHERE f.id = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return ResultSetMapper.mapFlight(rs);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Flight> findAll() {
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
            return flights;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void save(Flight flight) {
        String query =
                """
                INSERT INTO flights (departure, destination, departureDate, duration, seatRowsAmount, twoWay) VALUES
                (?, ?, ?, ?, ?, ?)
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
            ps.setBoolean(5, flight.getTwoWay());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Flight flight) {
        String query =
                """
                UPDATE flights
                SET departure = ?, destination = ?, departureDate = ?, duration = ?, seatRowsAmount = ?, twoWay = ?
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
            ps.setBoolean(5, flight.getTwoWay());
            ps.setInt(5, flight.getId());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
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
            e.printStackTrace();
        }
    }


    @Override
    public Boolean existsById(int id) {
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
            return rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
