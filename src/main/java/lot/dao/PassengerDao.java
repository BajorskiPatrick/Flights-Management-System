package lot.dao;

import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Passenger;
import lot.dao.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Passenger entities, providing database operations.
 */
public class PassengerDao implements GenericDao<Passenger> {
    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public PassengerDao() {}

    /**
     * {@inheritDoc}
     */
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

    /**
     * Finds passengers by surname.
     *
     * @param surname the surname to search for
     * @return a list of passengers matching the surname
     * @throws DatabaseActionException if a database error occurs
     */
    public List<Passenger> findBySurname(String surname) throws DatabaseActionException {
        String query =
                """
                SELECT *
                FROM passengers p
                WHERE p.surname = ?
                """;

        try (
                Connection conn = DatabaseInitializer.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, surname);
            ResultSet rs = ps.executeQuery();
            List<Passenger> passengers = new ArrayList<>();
            while (rs.next()) {
                Passenger passenger = ResultSetMapper.mapPassenger(rs);
                passengers.add(passenger);
            }
            rs.close();
            return passengers;
        }
        catch (SQLException e) {
            throw new DatabaseActionException("Database error while fetching passenger details", e);
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findAllId() throws DatabaseActionException {
        List<Integer> ids = new ArrayList<>();

        String query =
                """
                SELECT id
                FROM passengers
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
