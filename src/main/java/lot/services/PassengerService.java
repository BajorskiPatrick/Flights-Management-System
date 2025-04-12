package lot.services;

import lot.dao.PassengerDao;
import lot.models.Passenger;

import java.util.List;

public class PassengerService {
    private final PassengerDao passengerDao;

    public PassengerService(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }


    public void addNewPassenger(String name, String surname, String email, String phoneNumber) {

    }


    public List<Passenger> getAllPassengers() {
        return null;
    }


    public Passenger getPassengerById(int id) {
        return null;
    }


    public void updateExistingPassenger(int passengerId, String name, String surname, String email, String phoneNumber) {

    }


    public void deletePassenger(int passengerId) {

    }
}
