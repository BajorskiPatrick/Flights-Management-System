package lot.utils;

import lot.models.Flight;
import lot.models.Passenger;
import lot.models.Reservation;
import lot.models.Seat;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMapper {
    public static Flight mapFlight(ResultSet rs) throws SQLException {
        return new Flight(
                rs.getInt("id"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getInt("duration"),
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
                rs.getString("seatNumber"),
                rs.getBoolean("tookPlace")
        );
    }


    public static Seat mapSeat(ResultSet rs) throws SQLException {
        return new Seat(
                rs.getInt("flightId"),
                rs.getString("seatNumber"),
                rs.getBoolean("isAvailable")
        );
    }
}
