package lot.dao;

import lot.config.DatabaseInitializer;
import lot.models.Passenger;
import lot.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDao implements GenericDao<Passenger> {
    @Override
    public Passenger findById(int id) {
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
            return ResultSetMapper.mapPassenger(rs);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Passenger> findAll() {
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
            return passengers;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void save(Passenger passenger) {
        String query =
                """
                INSERT INTO passengers (name, surname, email, phoneNumber) VALUES 
                (?, ?, ?, ?)
                """;
        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, passenger.getName());
            ps.setString(2, passenger.getSurname());
            ps.setString(3, passenger.getEmail());
            ps.setString(4, passenger.getPhoneNumber());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Passenger passenger) {
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
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
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
            e.printStackTrace();
        }
    }

    @Override
    public Boolean existsById(int id) {
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
            return rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

