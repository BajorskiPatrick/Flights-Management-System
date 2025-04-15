package lot.services;

import lot.dao.FlightDao;
import lot.dao.PassengerDao;
import lot.dao.ReservationDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.EmailException;
import lot.exceptions.services.NotFoundException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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
//        validateData(flightId, passengerId, seatNumber, null);
        try {
            Reservation reservation = new Reservation(flightId, passengerId, seatNumber);
            int id = reservationDao.save(reservation);
            return id;

        } catch (DatabaseActionException e) {
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
            if (!reservationDao.existsById(reservationId)) {
                throw new NotFoundException("Reservation with id: " + reservationId + " can not be fetched, because it does not exists in the database");
            }
            return reservationDao.findById(reservationId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservation by its id due to some database problem", e);
        }
    }

    public List<Reservation> getReservationsByFlightId(int flightId) {
        try {
            if (!flightDao.existsById(flightId)) {
                throw new NotFoundException("Reservations with flight's id: " + flightId + " can not be fetched, because it does not exists in the database");
            }
            List<Reservation> reservations = reservationDao.findAllByForeignKey("flights", flightId);
            if (reservations.isEmpty()) {
                throw new NotFoundException("Flight with id: " + flightId + " does not have any reservations yet");
            }
            return reservations;
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservations by assigned flight id due to some database problem", e);
        }
    }


    public List<Reservation> getReservationsByPassengerId(int passengerId) {
        try {
            if (!passengerDao.existsById(passengerId)) {
                throw new NotFoundException("Reservations with passenger's id: " + passengerId + " can not be fetched, because it does not exists in the database");
            }
            List<Reservation> reservations = reservationDao.findAllByForeignKey("passengers", passengerId);
            if (reservations.isEmpty()) {
                throw new NotFoundException("Passenger with id: " + passengerId + " does not have any reservations yet");
            }
            return reservations;
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch reservations by assigned passenger's id due to some database problem", e);
        }
    }


    public void updateExistingReservation(int reservationId, int flightId, int passengerId, String seatNumber) {
        try {
            if (!reservationDao.existsById(reservationId)) {
                throw new NotFoundException("Reservation with id: " + reservationId + " can not be updated, because it does not exists in the database");
            }
//            validateData(flightId, passengerId, seatNumber, reservationId);
            Reservation reservation = new Reservation(reservationId, flightId, passengerId, seatNumber);
            reservationDao.update(reservation);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to update reservation due to some database problem", e);
        }
    }


    public void deleteReservation(int reservationId) {
        try {
            if (!reservationDao.existsById(reservationId)) {
                throw new NotFoundException("Reservation with id: " + reservationId + " can not be deleted, because it does not exists in the database");
            }
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


//    public void validateData(int flightId, int passengerId, String seatNumber, Integer reservationId) {
//        try {
//            if (!flightDao.existsById(flightId)) {
//                throw new ValidationException("Flight assigned to the reservation must exists!");
//            }
//            if (!passengerDao.existsById(passengerId)) {
//                throw new ValidationException("Passenger assigned to the reservation must exists!");
//            }
//            if (reservationId != null) {
//                if (!reservationDao.findById(reservationId).getSeatNumber().equals(seatNumber) && !flightDao.getAvailableSeatsNumbers(flightId).contains(seatNumber)) {
//                    throw new ValidationException("Provided seat number is not available or it does not exist");
//                }
//            }
//            else {
//                if (!flightDao.getAvailableSeatsNumbers(flightId).contains(seatNumber)) {
//                    throw new ValidationException("Provided seat number is not available or it does not exist");
//                }
//            }
//        }
//        catch (DatabaseActionException e) {
//            throw new ServiceException("Failed to validate data while creating or updating reservation due to some database problem", e);
//        }
//    }

    public void sendEmail(int reservationId) {
        try {
            Reservation reservation = reservationDao.findById(reservationId);
            emailService.sendConfirmationEmail(passengerDao.findById(reservation.getPassengerId()).getEmail(), reservation.toString());
        }
        catch (DatabaseActionException e) {
            throw new EmailException("Failed to collect data required to send email due to some database problem", e);
        }
        catch (NotFoundException e) {
            throw new EmailException("Failed to send email about reservation with id: " + reservationId + ", because it was not found ", e);
        }
    }
}
