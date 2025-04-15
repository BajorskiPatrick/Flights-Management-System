package lot.services;

import lot.dao.FlightDao;
import lot.dao.PassengerDao;
import lot.dao.ReservationDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.EmailException;
import lot.exceptions.services.ServiceException;
import lot.models.Reservation;

import java.util.List;

public class ReservationService {
    private final ReservationDao reservationDao;
    private final FlightDao flightDao;
    private final PassengerDao passengerDao;
    private final EmailService emailService;

    public ReservationService(ReservationDao rd, FlightDao fd, PassengerDao pd, EmailService emailService) {
        this.reservationDao = rd;
        this.flightDao = fd;
        this.passengerDao = pd;
        this.emailService = emailService;
    }

    public int makeNewReservation(int flightId, int passengerId, String seatNumber) {
        try {
            Reservation reservation = new Reservation(flightId, passengerId, seatNumber);
            return reservationDao.save(reservation);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to create new reservation due to some database problem", e);
        }
    }

    public List<Reservation> getAllReservations() {
        try {
            return reservationDao.findAll();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all reservations due to some database problem", e);
        }
    }

    public List<Integer> getIds() {
        try {
            return reservationDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all reservations ids due to some database problem", e);
        }
    }

    public List<Integer> getFlightIds() {
        try {
            return flightDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all flights ids due to some database problem", e);
        }
    }

    public List<Integer> getPassengerIds() {
        try {
            return passengerDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all passengers ids due to some database problem", e);
        }
    }

    public Reservation getReservationById(int reservationId) {
        try {
            return reservationDao.findById(reservationId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservation by its id due to some database problem", e);
        }
    }

    public List<Reservation> getReservationsByFlightId(int flightId) {
        try {
            return reservationDao.findAllByForeignKey("flights", flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservations by assigned flight id due to some database problem", e);
        }
    }

    public List<Reservation> getReservationsByPassengerId(int passengerId) {
        try {
            return reservationDao.findAllByForeignKey("passengers", passengerId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservations by assigned passenger's id due to some database problem", e);
        }
    }

    public List<Reservation> getReservationBySurname(String surname) {
        try {
            return reservationDao.findAllBySurname(surname);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservations by assigned passenger's surname due to some database problem", e);
        }
    }

    public void updateExistingReservation(int reservationId, int flightId, int passengerId, String seatNumber) {
        try {
            Reservation reservation = new Reservation(reservationId, flightId, passengerId, seatNumber);
            reservationDao.update(reservation);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to update reservation due to some database problem", e);
        }
    }

    public void deleteReservation(int reservationId) {
        try {
            reservationDao.delete(reservationId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to delete reservation by its id due to some database problem", e);
        }
    }

    public List<String> getAvailableSeats(int flightId) {
        try {
            return flightDao.getAvailableSeatsNumbers(flightId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch available seats numbers for selected flight with id: " + flightId + " due to some database problem", e);
        }
    }

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
