package lot.dao;

import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Flight;
import lot.models.Seat;
import lot.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class FlightDao implements GenericDao<Flight> {
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
            throw new DatabaseActionException("Database error while fetching flight details", e);
        }
    }


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


    @Override
    public int save(Flight flight) throws DatabaseActionException {
        String query =
                """
                INSERT INTO flights (departure, destination, departureDate, duration, seatRowsAmount, twoWay) VALUES
                (?, ?, ?, ?, ?, ?)
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
            ps.setBoolean(6, flight.getTwoWay());

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

    @Override
    public void update(Flight flight) throws DatabaseActionException {
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
            ps.setBoolean(6, flight.getTwoWay());
            ps.setInt(7, flight.getId());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while updating flight details", e);
        }
    }

    public void update(Flight flight, int previousSeatRowsAmount) throws DatabaseActionException {
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
            ps.setBoolean(6, flight.getTwoWay());
            ps.setInt(7, flight.getId());

            ps.executeUpdate();

            createSeats(conn, flight.getId(), previousSeatRowsAmount + 1, flight.getSeatRowsAmount());
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while updating flight details", e);
        }
    }

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
                        ps.setString(2, String.valueOf(i) + letters[j]);
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
