package lot;

import lot.dao.FlightDao;
import lot.dao.PassengerDao;
import lot.dao.ReservationDao;
import lot.database.DatabaseInitializer;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.ServiceException;
import lot.models.Seat;
import lot.services.EmailService;
import lot.services.FlightService;
import lot.services.PassengerService;
import lot.services.ReservationService;
import lot.utils.ResultSetMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


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

        //(1, 1, '1A'),

        ReservationService rs = new ReservationService(new ReservationDao(), new FlightDao(), new PassengerDao(), new EmailService());
        FlightService fs = new FlightService(new FlightDao());
        PassengerService ps = new PassengerService(new PassengerDao());
        try {
//            System.out.println(rs.getReservationsByPassengerId(1));
//            ps.deletePassenger(1);
//            System.out.println(rs.getReservationsByPassengerId(1));
            int passId = ps.addNewPassenger("Patrick", "Bajorski", "bajorski16@gmail.com", "790356300");
            int resId = rs.makeNewReservation(5, passId, "7F");
            System.out.println(ps.getPassengerById(passId));
            System.out.println(rs.getReservationById(resId));
        }
        catch (ServiceException e) {
            e.printStackTrace();
        }

//        try {
//            System.out.println(rs.getReservationById(1));
//            System.out.println("-------------------------------------");
//            System.out.println(fs.getAvailableSeats(1));
//
//            System.out.println();
//            System.out.println();
//
//            rs.updateExistingReservation(1, 1, 1, "7B");
//            System.out.println(rs.getReservationById(1));
//            System.out.println("-------------------------------------");
//            System.out.println(fs.getAvailableSeats(1));
//
//            System.out.println();
//            System.out.println();
//
//            rs.makeNewReservation(3, 1, "7B");
//            System.out.println(rs.getAllReservations());
//            System.out.println("-------------------------------------");
//            System.out.println(fs.getAvailableSeats(3));
//        }
//        catch (ServiceException e) {
//            System.err.println(e.getMessage());
//        }


//        String query1 =
//                """
//                SELECT *
//                FROM seats
//                WHERE flightId = 1
//                """;
//
//        String query2 =
//                """
//                DELETE FROM reservations
//                WHERE flightId = 1 AND passengerId = 1
//                """;
//
//
//        try (
//                Connection conn = DatabaseInitializer.getConnection();
//                Statement stmt = conn.createStatement();
//        ) {
//            ResultSet rs1 = stmt.executeQuery(query1);
//            while (rs1.next()) {
//                System.out.println(ResultSetMapper.mapSeat(rs1));
//            }
//            rs1.close();
//
//            System.out.println();
//
//            stmt.executeUpdate(query2);
//
//            ResultSet rs2 = stmt.executeQuery(query1);
//            while (rs2.next()) {
//                System.out.println(ResultSetMapper.mapSeat(rs2));
//            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }

//        EmailService emailService = new EmailService();
//        emailService.sendConfirmationEmail("bajorski16@gmail.com", "Testowy email");

//        FlightService fs = new FlightService(new FlightDao());
//        try {
//            List<Flight> flights = fs.getAllFlights();
//            for (Flight flight : flights) {
//                System.out.println(flight);
//            }
//            System.out.println();
//
//            fs.addNewFlight("Kraków", "Dallas", "2025-06-15 12:00", -1, 10, true);
//
//            List<Flight> flights2 = fs.getAllFlights();
//            for (Flight flight : flights2) {
//                System.out.println(flight);
//            }
//            System.out.println();
//
////            fs.updateExistingFlight(11,"Kraków", "Dallas", "2025-06-15 12:00", 800, 10, true);
////
////            List<Flight> flights3 = fs.getAllFlights();
////            for (Flight flight : flights3) {
////                System.out.println(flight);
////            }
////            System.out.println();
////
////            fs.deleteFlight(11);
////
////            List<Flight> flights4 = fs.getAllFlights();
////            for (Flight flight : flights4) {
////                System.out.println(flight);
////            }
////            System.out.println();
//        }
//        catch (ServiceException e) {
//            e.printStackTrace();
//        }
//        catch (NotFoundException e) {
//            System.err.println(e.getMessage());
//        }
//        catch (ValidationException e) {
//            System.err.println(e.getMessage());
//        }
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