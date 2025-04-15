package lot.services;

import lot.dao.PassengerDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.NotFoundException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Passenger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;

public class PassengerService {
    private final PassengerDao passengerDao;

    public PassengerService(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }


    public int addNewPassenger(String name, String surname, String email, String phoneNumber) {
        validateData(email, phoneNumber);
        Passenger newPassenger = new Passenger(name, surname, email, phoneNumber);
        try {
            int id = passengerDao.save(newPassenger);
            return id;
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to save new passenger due to some database problem", e);
        }
    }


    public List<Passenger> getAllPassengers() {
        try {
            return passengerDao.findAll();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all passengers due to some database problem", e);
        }
    }

    public List<Integer> getIds() {
        try {
            return passengerDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all passengers ids due to some database problem", e);
        }
    }


    public Passenger getPassengerById(int id) {
        try {
            return passengerDao.findById(id);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch passenger with id: " + id + " due to some database problem", e);
        }
    }


    public void updateExistingPassenger(int passengerId, String name, String surname, String email, String phoneNumber) {
        validateData(email, phoneNumber);
        Passenger passenger = new Passenger(passengerId, name, surname, email, phoneNumber);
        try {
            if (!passengerDao.existsById(passengerId)) {
                throw new NotFoundException("Passenger with id: " + passengerId + " can not be updated, because it does not exist in the database");
            }
            passengerDao.update(passenger);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to update passenger with id:" + passengerId + " due to some database problem", e);
        }
    }

    public void deletePassenger(int passengerId) {
        try {
            if (!passengerDao.existsById(passengerId)) {
                throw new NotFoundException("Passenger with id: " + passengerId  + " can not be deleted, because it does not exists in the database");
            }
            passengerDao.delete(passengerId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to delete passenger with id: " + passengerId + " due to some database problem", e);
        }
    }


    private void validateData(String email, String phoneNumber) {
        Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$");
        Pattern phoneNumberPattern = Pattern.compile("^[1-9][0-9]{8}$");
        Matcher emailMatcher = emailPattern.matcher(email);
        Matcher phoneNumberMatcher = phoneNumberPattern.matcher(phoneNumber);

        if (!emailMatcher.matches()) {
            throw new ValidationException("Invalid email format!");
        }
        if (!phoneNumberMatcher.matches()) {
            throw new ValidationException("Invalid phone number format! Accepted format is: XXXXXXXXX");
        }
    }
}
