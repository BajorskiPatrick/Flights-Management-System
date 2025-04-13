package lot;

import lot.config.DatabaseInitializer;
import lot.dao.FlightDao;
import lot.dao.ReservationDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.models.Flight;
import lot.models.Reservation;
import lot.services.ReservationService;
import lot.utils.ResultSetMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class App {
    public static void main(String[] args) {
        try {
            DatabaseInitializer.initialize();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        FlightDao fd = new FlightDao();
        ReservationDao rd = new ReservationDao();
        try {
            Flight f = fd.findById(2);
//            Reservation r1 = rd.findById(1);
//            Reservation r2 = rd.findById(2);
//            Reservation r3 = rd.findById(3);
            System.out.println(f);
//            System.out.println(r1);
//            System.out.println(r2);
//            System.out.println(r3);
//
//            fd.delete(1);

//            Reservation r1b = rd.findById(1);
//            Reservation r2b = rd.findById(2);
//            Reservation r3b = rd.findById(3);
//            System.out.println(r1b);
//            System.out.println(r2b);
//            System.out.println(r3b);

        } catch (DatabaseActionException e) {
            System.err.println(e.getMessage());
        }
    }
}


//        try (Connection conn = DatabaseInitializer.getConnection()) {
//            Statement stmt = conn.createStatement();
//            String query = "select * from reservations r where r.passengerId = 1";
//            ResultSet rs = stmt.executeQuery(query);
//            while (rs.next()) {
//                Reservation res = ResultSetMapper.mapReservation(rs);
//                System.out.println(res);
//            }
//            stmt.close();
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }