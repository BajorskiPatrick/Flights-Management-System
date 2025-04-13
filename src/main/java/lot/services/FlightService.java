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

    public int addNewFlight(String departure, String destination, String departureDate, int duration, int seatRowsAmount, Boolean twoWay) {
        validateData(departureDate, duration, seatRowsAmount);
        LocalDateTime dd = LocalDateTime.parse(departureDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Flight flight = new Flight(departure, destination, dd, duration, seatRowsAmount, twoWay);

        try {
            int id = flightDao.save(flight);
            return id;
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to save new flight due to some database problem", e);
        }
    }


    public List<Flight> getAllFlights() {
        try {
            return flightDao.findAll();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all flights due to some database problem", e);
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
            throw new ServiceException("Failed to fetch flight with id: " + flightId + " due to some database problem", e);
        }
    }

    public List<String> getAvailableSeats(int flightId) {
        try {
            return flightDao.getAvailableSeatsNumbers(flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch available seats numbers for flight with id: " + flightId + " due to some database problem", e);
        }
    }


    public void updateExistingFlight(int flightId, String departure, String destination, String departureDate, int duration, int seatRowsAmount, Boolean twoWay) {
        Flight flight;
        try {
            flight = getFlightById(flightId);
        }
        catch (NotFoundException e) {
            throw new NotFoundException("Flight with id: " + flightId + " can not be updated, because it does not exists in the database");
        }

        validateData(departureDate, duration, seatRowsAmount);
        LocalDateTime dd = LocalDateTime.parse(departureDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        if (seatRowsAmount < flight.getSeatRowsAmount()) {
            throw new ValidationException("New seat rows amount must be greater or equal to previous seat rows amount");
        }


        try {
            Flight newFlight = new Flight(flightId, departure, destination, dd, duration, seatRowsAmount, twoWay);
            if (seatRowsAmount == flight.getSeatRowsAmount()) {
                flightDao.update(newFlight);
            }
            else {
                flightDao.update(newFlight, flight.getSeatRowsAmount());
            }
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to update flight with id: " + flightId + " due to some database problem", e);
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
            throw new ServiceException("Failed to delete flight with id: " + flightId + " due to some database problem", e);
        }
    }


    private void validateData(String departureDate, int duration, int seatRowsAmount) {
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
