package lot.services;

import lot.dao.PassengerDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Passenger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;

/**
 * Service class for handling passenger-related operations.
 */
public class PassengerService {
    private final PassengerDao passengerDao;

    /**
     * Constructs a PassengerService with the specified PassengerDao.
     *
     * @param passengerDao the data access object for passengers
     */
    public PassengerService(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }

    /**
     * Adds a new passenger to the system.
     *
     * @param name the passenger's first name
     * @param surname the passenger's last name
     * @param email the passenger's email address
     * @param phoneNumber the passenger's phone number
     * @return the ID of the newly created passenger
     * @throws ValidationException if the email or phone number is invalid
     * @throws ServiceException if there is a database error
     */
    public int addNewPassenger(String name, String surname, String email, String phoneNumber) {
        validateData(email, phoneNumber);
        Passenger newPassenger = new Passenger(name, surname, email, phoneNumber);
        try {
            return passengerDao.save(newPassenger);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to save new passenger due to some database problem", e);
        }
    }

    /**
     * Retrieves all passengers from the system.
     *
     * @return a list of all passengers
     * @throws ServiceException if there is a database error
     */
    public List<Passenger> getAllPassengers() {
        try {
            return passengerDao.findAll();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all passengers due to some database problem", e);
        }
    }

    /**
     * Retrieves all passenger IDs from the system.
     *
     * @return a list of all passenger IDs
     * @throws ServiceException if there is a database error
     */
    public List<Integer> getIds() {
        try {
            return passengerDao.findAllId();
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch all passengers ids due to some database problem", e);
        }
    }

    /**
     * Retrieves a passenger by their ID.
     *
     * @param id the ID of the passenger to retrieve
     * @return the passenger with the specified ID
     * @throws ServiceException if there is a database error
     */
    public Passenger getPassengerById(int id) {
        try {
            return passengerDao.findById(id);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch passenger with id: " + id + " due to some database problem", e);
        }
    }

    /**
     * Retrieves passengers by surname.
     *
     * @param surname the surname to search for
     * @return a list of passengers matching the surname
     * @throws ServiceException if there is a database error
     */
    public List<Passenger> getPassengerBySurname(String surname) {
        try {
            return passengerDao.findBySurname(surname);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to fetch passenger with surname: " + surname + " due to some database problem", e);
        }
    }

    /**
     * Updates an existing passenger in the system.
     *
     * @param passengerId the ID of the passenger to update
     * @param name the new first name
     * @param surname the new last name
     * @param email the new email address
     * @param phoneNumber the new phone number
     * @throws ValidationException if the email or phone number is invalid
     * @throws ServiceException if there is a database error
     */
    public void updateExistingPassenger(int passengerId, String name, String surname, String email, String phoneNumber) {
        validateData(email, phoneNumber);
        Passenger passenger = new Passenger(passengerId, name, surname, email, phoneNumber);
        try {
            if (!passengerDao.existsById(passengerId)) {
                throw new ValidationException("Passenger with id: " + passengerId + " can not be updated, because it does not exist in the database");
            }
            passengerDao.update(passenger);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to update passenger with id:" + passengerId + " due to some database problem", e);
        }
    }

    /**
     * Deletes a passenger from the system.
     *
     * @param passengerId the ID of the passenger to delete
     * @throws ServiceException if there is a database error
     * @throws ValidationException if there is a validation error
     */
    public void deletePassenger(int passengerId) {
        try {
            if (!passengerDao.existsById(passengerId)) {
                throw new ValidationException("Passenger with id: " + passengerId + " can not be updated, because it does not exist in the database");
            }
            passengerDao.delete(passengerId);
        }
        catch (DatabaseActionException e) {
            throw new ServiceException("Failed to delete passenger with id: " + passengerId + " due to some database problem", e);
        }
    }

    /**
     * Validates passenger email and phone number.
     *
     * @param email the email to validate
     * @param phoneNumber the phone number to validate
     * @throws ValidationException if either the email or phone number is invalid
     */
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
