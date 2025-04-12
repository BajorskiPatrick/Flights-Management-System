package lot;

import lot.config.DatabaseInitializer;

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
            String query = "select * from flights";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                int time = rs.getInt("duration");
                System.out.println("ID: " + id + "\tTime: " + time);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}