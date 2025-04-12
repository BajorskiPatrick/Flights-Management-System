package lot.services;

import lot.dao.FlightDao;
import lot.dao.PassengerDao;
import lot.dao.ReservationDao;
import lot.models.Reservation;

import java.util.List;

public class ReservationService {
    private final ReservationDao reservationDao;
    private final FlightDao flightDao;
    private final PassengerDao passengerDao;

    public ReservationService(ReservationDao rd, FlightDao fd, PassengerDao pd) {
        this.reservationDao = rd;
        this.flightDao = fd;
        this.passengerDao = pd;
    }


    public void makeNewReservation(int flightId, int passengerId, String seatNumber) {

    }


    public List<Reservation> getAllReservations() {
        return null;
    }


    public List<Reservation> getReservationsByFlightId(int flightId) {
        return null;
    }


    public List<Reservation> getReservationsByPassengerId(int passengerId) {
        return null;
    }


    public void updateExistingReservation(int reservationId, int flightId, int passengerId, String seatNumber) {

    }


    public void deleteReservation(int reservationId) {

    }
}
