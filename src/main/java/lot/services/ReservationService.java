package lot.services;

import lot.dao.FlightDao;
import lot.dao.PassengerDao;
import lot.dao.ReservationDao;

public class ReservationService {
    private final ReservationDao reservationDao;
    private final FlightDao flightDao;
    private final PassengerDao passengerDao;

    public ReservationService(ReservationDao rd, FlightDao fd, PassengerDao pd) {
        this.reservationDao = rd;
        this.flightDao = fd;
        this.passengerDao = pd;
    }
}
