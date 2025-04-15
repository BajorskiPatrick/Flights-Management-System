package lot.utils;

import lot.dao.ReservationDao;
import lot.models.Flight;
import lot.models.Passenger;
import lot.models.Reservation;
import lot.models.Seat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResultSetMapper {
    public static Flight mapFlight(ResultSet rs) throws SQLException {
        return new Flight(
                rs.getInt("id"),
                rs.getString("departure"),
                rs.getString("destination"),
                rs.getObject("departureDate", LocalDateTime.class),
                rs.getInt("duration"),
                rs.getInt("seatRowsAmount"),
                rs.getBoolean("twoWay")
        );
    }


    public static Passenger mapPassenger(ResultSet rs) throws SQLException {
        return new Passenger(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("email"),
                rs.getString("phoneNumber")
        );
    }


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


    public static Seat mapSeat(ResultSet rs) throws SQLException {
        return new Seat(
                rs.getInt("flightId"),
                rs.getString("seatNumber"),
                rs.getBoolean("available")
        );
    }
}
