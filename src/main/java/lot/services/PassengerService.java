package lot.services;

import lot.dao.PassengerDao;

public class PassengerService {
    private final PassengerDao passengerDao;

    public PassengerService(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }
}
