package lot.services;

import lot.dao.FlightDao;
import lot.dao.PassengerDao;
import lot.dao.ReservationDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.EmailException;
import lot.exceptions.services.ServiceException;
import lot.models.Reservation;

import java.util.List;

/**
 * Service class for handling reservation-related operations.
 */
public class ReservationService {
    private final ReservationDao reservationDao;
    private final FlightDao flightDao;
    private final PassengerDao passengerDao;
    private final EmailService emailService;

    /**
     * Constructs a ReservationService with the specified DAOs and services.
     *
     * @param rd the reservation data access object
     * @param fd the flight data access object
     * @param pd the passenger data access object
     * @param emailService the email service
     */
    public ReservationService(ReservationDao rd, FlightDao fd, PassengerDao pd, EmailService emailService) {
        this.reservationDao = rd;
        this.flightDao = fd;
        this.passengerDao = pd;
        this.emailService = emailService;
    }

    /**
     * Creates a new reservation in the system.
     *
     * @param flightId the ID of the flight to reserve
     * @param passengerId the ID of the passenger making the reservation
     * @param seatNumber the seat number to reserve
     * @return the ID of the newly created reservation
     * @throws ServiceException if there is a database error
     */
    public int makeNewReservation(int flightId, int passengerId, String seatNumber) {
        try {
            Reservation reservation = new Reservation(flightId, passengerId, seatNumber);
            return reservationDao.save(reservation);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to create new reservation due to some database problem", e);
        }
    }

    /**
     * Retrieves all reservations from the system.
     *
     * @return a list of all reservations
     * @throws ServiceException if there is a database error
     */
    public List<Reservation> getAllReservations() {
        try {
            return reservationDao.findAll();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all reservations due to some database problem", e);
        }
    }

    /**
     * Retrieves all reservation IDs from the system.
     *
     * @return a list of all reservation IDs
     * @throws ServiceException if there is a database error
     */
    public List<Integer> getIds() {
        try {
            return reservationDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all reservations ids due to some database problem", e);
        }
    }

    /**
     * Retrieves all flight IDs from the system.
     *
     * @return a list of all flight IDs
     * @throws ServiceException if there is a database error
     */
    public List<Integer> getFlightIds() {
        try {
            return flightDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all flights ids due to some database problem", e);
        }
    }

    /**
     * Retrieves all passenger IDs from the system.
     *
     * @return a list of all passenger IDs
     * @throws ServiceException if there is a database error
     */
    public List<Integer> getPassengerIds() {
        try {
            return passengerDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all passengers ids due to some database problem", e);
        }
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param reservationId the ID of the reservation to retrieve
     * @return the reservation with the specified ID
     * @throws ServiceException if there is a database error
     */
    public Reservation getReservationById(int reservationId) {
        try {
            return reservationDao.findById(reservationId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservation by its id due to some database problem", e);
        }
    }

    /**
     * Retrieves reservations by flight ID.
     *
     * @param flightId the ID of the flight to search for
     * @return a list of reservations for the specified flight
     * @throws ServiceException if there is a database error
     */
    public List<Reservation> getReservationsByFlightId(int flightId) {
        try {
            return reservationDao.findAllByForeignKey("flights", flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservations by assigned flight id due to some database problem", e);
        }
    }

    /**
     * Retrieves reservations by passenger ID.
     *
     * @param passengerId the ID of the passenger to search for
     * @return a list of reservations for the specified passenger
     * @throws ServiceException if there is a database error
     */
    public List<Reservation> getReservationsByPassengerId(int passengerId) {
        try {
            return reservationDao.findAllByForeignKey("passengers", passengerId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservations by assigned passenger's id due to some database problem", e);
        }
    }

    /**
     * Retrieves reservations by passenger surname.
     *
     * @param surname the surname to search for
     * @return a list of reservations for passengers with the specified surname
     * @throws ServiceException if there is a database error
     */
    public List<Reservation> getReservationBySurname(String surname) {
        try {
            return reservationDao.findAllBySurname(surname);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservations by assigned passenger's surname due to some database problem", e);
        }
    }

    /**
     * Updates an existing reservation in the system.
     *
     * @param reservationId the ID of the reservation to update
     * @param flightId the new flight ID
     * @param passengerId the new passenger ID
     * @param seatNumber the new seat number
     * @throws ServiceException if there is a database error
     */
    public void updateExistingReservation(int reservationId, int flightId, int passengerId, String seatNumber) {
        try {
            Reservation reservation = new Reservation(reservationId, flightId, passengerId, seatNumber);
            reservationDao.update(reservation);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to update reservation due to some database problem", e);
        }
    }

    /**
     * Deletes a reservation from the system.
     *
     * @param reservationId the ID of the reservation to delete
     * @throws ServiceException if there is a database error
     */
    public void deleteReservation(int reservationId) {
        try {
            reservationDao.delete(reservationId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to delete reservation by its id due to some database problem", e);
        }
    }

    /**
     * Retrieves available seats for a flight.
     *
     * @param flightId the ID of the flight to check
     * @return a list of available seat numbers
     * @throws ServiceException if there is a database error
     */
    public List<String> getAvailableSeats(int flightId) {
        try {
            return flightDao.getAvailableSeatsNumbers(flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch available seats numbers for selected flight with id: " + flightId + " due to some database problem", e);
        }
    }

    /**
     * Sends a confirmation email for a reservation.
     *
     * @param reservationId the ID of the reservation to send confirmation for
     * @throws EmailException if there is an error sending the email
     */
    public void sendEmail(int reservationId) {
        try {
            Reservation reservation = reservationDao.findById(reservationId);
            emailService.sendConfirmationEmail(passengerDao.findById(reservation.getPassengerId()).getEmail(), reservation.toString());
        }
        catch (DatabaseActionException e) {
            throw new EmailException("Failed to collect data required to send email due to some database problem", e);
        }
    }
}
