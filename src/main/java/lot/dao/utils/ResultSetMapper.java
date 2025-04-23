package lot.dao.utils;

import lot.models.Flight;
import lot.models.Passenger;
import lot.models.Reservation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Utility class for mapping database ResultSet objects to model objects.
 * Provides static methods to convert ResultSet rows into corresponding model instances.
 */
public class ResultSetMapper {
    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public ResultSetMapper() {}

    /**
     * Maps a ResultSet row to a Flight object.
     *
     * @param rs the ResultSet containing flight data (must be positioned at the desired row)
     * @return a new Flight object populated with data from the ResultSet
     * @throws SQLException if a database access error occurs or any column value is invalid
     */
    public static Flight mapFlight(ResultSet rs) throws SQLException {
        return new Flight(
                rs.getInt("id"),
                rs.getString("departure"),
                rs.getString("destination"),
                rs.getObject("departureDate", LocalDateTime.class),
                rs.getInt("duration"),
                rs.getInt("seatRowsAmount")
        );
    }

    /**
     * Maps a ResultSet row to a Passenger object.
     *
     * @param rs the ResultSet containing passenger data (must be positioned at the desired row)
     * @return a new Passenger object populated with data from the ResultSet
     * @throws SQLException if a database access error occurs or any column value is invalid
     */
    public static Passenger mapPassenger(ResultSet rs) throws SQLException {
        return new Passenger(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("email"),
                rs.getString("phoneNumber")
        );
    }

    /**
     * Maps a ResultSet row to a Reservation object.
     *
     * @param rs the ResultSet containing reservation data (must be positioned at the desired row)
     * @return a new Reservation object populated with data from the ResultSet
     * @throws SQLException if a database access error occurs or any column value is invalid
     */
    public static Reservation mapReservation(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("flightId"),
                rs.getInt("passengerId"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("seatNumber"),
                rs.getObject("departureDate", LocalDateTime.class)
        );
    }
}
