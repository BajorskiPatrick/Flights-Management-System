package lot.dao;

import lot.config.DatabaseInitializer;
import lot.models.Passenger;

import java.sql.Connection;
import java.sql.SQLException;

public class PassengerDao {
    private final Connection connection;

    public PassengerDao() throws SQLException {
        connection = DatabaseInitializer.getConnection();
    }

//    @Override
//    public Passenger findById(int id) {
//        return new Passenger();
//    }
}

