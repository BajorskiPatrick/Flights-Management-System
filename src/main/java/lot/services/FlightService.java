package lot.services;

import lot.dao.FlightDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.NotFoundException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Flight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FlightService {
    private final FlightDao flightDao;

    public FlightService(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    public void addNewFlight(String departure, String destination, String departureDate, int duration, int seatRowsAmount, Boolean twoWay) {
        validateData(departureDate, duration, seatRowsAmount);
        LocalDateTime dd = LocalDateTime.parse(departureDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Flight flight = new Flight(departure, destination, dd, duration, seatRowsAmount, twoWay);

        try {
            flightDao.save(flight);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to save flight", e);
        }
    }


    public List<Flight> getAllFlights() {
        try {
            return flightDao.findAll();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all flights", e);
        }
    }


    public Flight getFlightById(int flightId) {
        try {
            if (!flightDao.existsById(flightId)) {
                throw new NotFoundException("Flight with id: " + flightId + " can not be fetched, because it does not exists in the database");
            }
            return flightDao.findById(flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Database error! Failed to fetch flight with id: " + flightId, e);
        }
    }


    public void updateExistingFlight(int flightId, String departure, String destination, String departureDate, int duration, int seatRowsAmount, Boolean twoWay) {
        validateData(departureDate, duration, seatRowsAmount);
        LocalDateTime dd = LocalDateTime.parse(departureDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        try {
            if (!flightDao.existsById(flightId)) {
                throw new NotFoundException("Flight with id: " + flightId + " can not be updated, because it does not exists in the database");
            }
            Flight flight = new Flight(flightId, departure, destination, dd, duration, seatRowsAmount, twoWay);
            flightDao.update(flight);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to update flight with id: " + flightId, e);
        }
    }


    public void deleteFlight(int flightId) {
        try {
            if (!flightDao.existsById(flightId)) {
                throw new NotFoundException("Flight with id: " + flightId  + " can not be deleted, because it does not exists in the database");
            }
            flightDao.delete(flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to delete flight with id: " + flightId, e);
        }
    }


    public void validateData(String departureDate, int duration, int seatRowsAmount) {
        LocalDateTime dd;
        try {
            dd = LocalDateTime.parse(departureDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        catch (DateTimeParseException e) {
            throw new ValidationException("Provided date is in wrong format! Required date in format: yyyy-MM-dd HH:mm");
        }

        if (dd.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Flight's date must be in the future?");
        }
        if (duration <= 0) {
            throw new ValidationException("Flight's duration must be greater than 0!");
        }
        if (seatRowsAmount <= 0) {
            throw new ValidationException("Flight's seat rows amount must be greater than 0!");
        }
    }
}
