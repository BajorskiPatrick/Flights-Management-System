package lot.services;

import lot.dao.FlightDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Service class for handling flight-related operations.
 */
public class FlightService {
    private final FlightDao flightDao;

    /**
     * Constructs a FlightService with the specified FlightDao.
     *
     * @param flightDao the data access object for flights
     */
    public FlightService(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    /**
     * Adds a new flight to the system.
     *
     * @param departure the departure location
     * @param destination the destination location
     * @param departureDate the date of departure
     * @param time the time of departure (format: HH:mm)
     * @param duration the flight duration in minutes
     * @param seatRowsAmount the number of seat rows
     * @return the ID of the newly created flight
     * @throws ValidationException if the input data is invalid
     * @throws ServiceException if there is a database error
     */
    public int addNewFlight(String departure, String destination, LocalDate departureDate, String time, int duration, int seatRowsAmount) {
        validateData(departureDate, time, duration, seatRowsAmount);
        LocalTime t = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime dd = departureDate.atTime(t);
        Flight flight = new Flight(departure, destination, dd, duration, seatRowsAmount);

        try {
            return flightDao.save(flight);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to save new flight due to some database problem", e);
        }
    }

    /**
     * Retrieves all flights from the system.
     *
     * @return a list of all flights
     * @throws ServiceException if there is a database error
     */
    public List<Flight> getAllFlights() {
        try {
            return flightDao.findAll();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all flights due to some database problem", e);
        }
    }

    /**
     * Retrieves a flight by its ID.
     *
     * @param flightId the ID of the flight to retrieve
     * @return the flight with the specified ID
     * @throws ServiceException if there is a database error
     * @throws ValidationException if there is a validation error
     */
    public Flight getFlightById(int flightId) {
        try {
            if (!flightDao.existsById(flightId)) {
                throw new ValidationException("Flight with id: " + flightId + " does not exists in the database");
            }
            return flightDao.findById(flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch flight with id: " + flightId + " due to some database problem", e);
        }
    }

    /**
     * Retrieves flights by departure location.
     *
     * @param departure the departure location to search for
     * @return a list of flights matching the departure location
     * @throws ServiceException if there is a database error
     */
    public List<Flight> getFlightByDeparture(String departure) {
        try {
            return flightDao.findByDeparture(departure);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch flights with departure: " + departure + " due to some database problem", e);
        }
    }

    /**
     * Retrieves flights by destination location.
     *
     * @param destination the destination location to search for
     * @return a list of flights matching the destination location
     * @throws ServiceException if there is a database error
     */
    public List<Flight> getFlightByDestination(String destination) {
        try {
            return flightDao.findByDestination(destination);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch flights with destination: " + destination + " due to some database problem", e);
        }
    }

    /**
     * Retrieves flights by departure date.
     *
     * @param date the date to search for
     * @return a list of flights matching the departure date
     * @throws ServiceException if there is a database error
     */
    public List<Flight> getFlightByDate(LocalDate date) {
        try {
            return flightDao.findByDate(date);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch flights with date: " + date + " due to some database problem", e);
        }
    }

    /**
     * Retrieves all flight IDs from the system.
     *
     * @return a list of all flight IDs
     * @throws ServiceException if there is a database error
     */
    public List<Integer> getIds() {
        try {
            return flightDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all flights ids due to some database problem", e);
        }
    }

    /**
     * Updates an existing flight in the system.
     *
     * @param flightId the ID of the flight to update
     * @param departure the new departure location
     * @param destination the new destination location
     * @param departureDate the new departure date
     * @param time the new departure time (format: HH:mm)
     * @param duration the new flight duration
     * @param seatRowsAmount the new number of seat rows
     * @throws ValidationException if the input data is invalid
     * @throws ServiceException if there is a database error
     */
    public void updateExistingFlight(int flightId, String departure, String destination, LocalDate departureDate, String time, int duration, int seatRowsAmount) {
        Flight flight = getFlightById(flightId);

        validateData(departureDate, time, duration, seatRowsAmount);
        LocalTime t = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime dd = departureDate.atTime(t);

        if (seatRowsAmount < flight.getSeatRowsAmount()) {
            throw new ValidationException("New seat rows amount must be greater or equal to previous seat rows amount");
        }

        try {
            Flight newFlight = new Flight(flightId, departure, destination, dd, duration, seatRowsAmount);
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

    /**
     * Deletes a flight from the system.
     *
     * @param flightId the ID of the flight to delete
     * @throws ServiceException if there is a database error
     * @throws ValidationException if there is a validation error
     */
    public void deleteFlight(int flightId) {
        try {
            if (!flightDao.existsById(flightId)) {
                throw new ValidationException("Flight with id: " + flightId  + " can not be deleted, because it does not exists in the database");
            }
            flightDao.delete(flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to delete flight with id: " + flightId + " due to some database problem", e);
        }
    }

    /**
     * Validates flight data before processing.
     *
     * @param date the departure date
     * @param time the departure time
     * @param duration the flight duration
     * @param seatRowsAmount the number of seat rows
     * @throws ValidationException if any of the data is invalid
     */
    private void validateData(LocalDate date, String time, int duration, int seatRowsAmount) {
        LocalTime t;
        try {
            t = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        }
        catch (DateTimeParseException e) {
            throw new ValidationException("Provided time is in wrong format! Required time in format: HH:mm");
        }

        LocalDateTime dt = date.atTime(t);

        if (dt.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Flight's date must be in the future!");
        }
        if (duration <= 0) {
            throw new ValidationException("Flight's duration must be greater than 0!");
        }
        if (seatRowsAmount <= 0) {
            throw new ValidationException("Flight's seat rows amount must be greater than 0!");
        }
    }
}
