package lot.services;

import lot.dao.FlightDao;

import java.time.LocalDateTime;

public class FlightService {
    private final FlightDao flightDao;

    public FlightService(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    public void addNewFlight(String departure, String destination, LocalDateTime DepartureDate, int duration, int seatAmount, Boolean twoWay) {

    }
}
