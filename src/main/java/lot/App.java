package lot;

import lot.config.DatabaseInitializer;
import lot.models.Flight;
import lot.models.Reservation;
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

        try (Connection conn = DatabaseInitializer.getConnection()) {
            Statement stmt = conn.createStatement();
            String query = "select * from reservations r where r.passengerId = 1";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Reservation res = ResultSetMapper.mapReservation(rs);
                System.out.println(res);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}