package lot.dao;

import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Passenger;
import lot.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDao implements GenericDao<Passenger> {
    @Override
    public Passenger findById(int id) throws DatabaseActionException {
        String query =
                """
                SELECT *
                FROM passengers p
                WHERE p.id = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Passenger passenger = ResultSetMapper.mapPassenger(rs);
            rs.close();
            return passenger;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching passenger details", e);
        }
    }


    @Override
    public List<Passenger> findAll() throws DatabaseActionException {
        List<Passenger> passengers = new ArrayList<>();

        String query =
                """
                SELECT *
                FROM passengers
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                Statement statement = conn.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                passengers.add(ResultSetMapper.mapPassenger(rs));
            }
            rs.close();
            return passengers;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching all passengers details", e);
        }
    }


    @Override
    public int save(Passenger passenger) throws DatabaseActionException {
        String query =
                """
                INSERT INTO passengers (name, surname, email, phoneNumber) VALUES 
                (?, ?, ?, ?)
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, passenger.getName());
            ps.setString(2, passenger.getSurname());
            ps.setString(3, passenger.getEmail());
            ps.setString(4, passenger.getPhoneNumber());

            ps.executeUpdate();

            int newId;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                } else {
                    throw new DatabaseActionException("No generated ID received after saving new passenger");
                }
            }
            return newId;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while saving new passenger", e);
        }
    }

    @Override
    public void update(Passenger passenger) throws DatabaseActionException {
        String query =
                """
                UPDATE passengers
                SET name = ?, surname = ?, email = ?, phoneNumber = ?
                WHERE id = ?
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, passenger.getName());
            ps.setString(2, passenger.getSurname());
            ps.setString(3, passenger.getEmail());
            ps.setString(4, passenger.getPhoneNumber());
            ps.setInt(5, passenger.getId());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while updating passenger details", e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseActionException {
        String query =
                """
                DELETE FROM passengers
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
            throw new DatabaseActionException("Database error while deleting passenger details", e);
        }
    }

    @Override
    public Boolean existsById(int id) throws DatabaseActionException {
        String query =
                """
                SELECT 1
                FROM passengers p
                WHERE p.id = ?
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
            throw new DatabaseActionException("Database error while checking if passenger exists", e);
        }
    }
}

