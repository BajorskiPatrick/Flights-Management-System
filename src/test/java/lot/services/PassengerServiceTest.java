package lot.services;

import lot.dao.PassengerDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    private PassengerDao passengerDao;

    @InjectMocks
    private PassengerService passengerService;

    private Passenger testPassenger;

    @BeforeEach
    void setUp() {
        testPassenger = new Passenger("John", "Doe", "john.doe@example.com", "123456789");
        testPassenger.setId(1);
    }

    @Test
    void addNewPassenger_ShouldReturnPassengerId_WhenDataIsValid() throws DatabaseActionException {
        when(passengerDao.save(any(Passenger.class))).thenReturn(1);

        int result = passengerService.addNewPassenger("John", "Doe", "john.doe@example.com",
                "123456789");

        assertEquals(1, result);
        verify(passengerDao).save(any(Passenger.class));
    }

    @Test
    void addNewPassenger_ShouldThrowValidationException_WhenPhoneNumberIsInvalid() {
        assertThrows(ValidationException.class, () ->
                passengerService.addNewPassenger("John", "Doe", "john.doe@example.com",
                        "012345678")
        );
    }

    @Test
    void addNewPassenger_ShouldThrowValidationException_WhenEmailIsInvalid() {
        assertThrows(ValidationException.class, () ->
                passengerService.addNewPassenger("John", "Doe", "invalid-email",
                        "123456789")
        );
    }

    @Test
    void addNewPassenger_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.save(any(Passenger.class))).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                passengerService.addNewPassenger("John", "Doe", "john.doe@example.com",
                        "123456789")
        );
    }

    @Test
    void getAllPassengers_ShouldReturnPassengerList() throws DatabaseActionException {
        when(passengerDao.findAll()).thenReturn(Arrays.asList(testPassenger));

        List<Passenger> result = passengerService.getAllPassengers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    void getAllPassengers_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.findAll()).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                passengerService.getAllPassengers()
        );
    }

    @Test
    void getIds_ShouldReturnIdList() throws DatabaseActionException {
        when(passengerDao.findAllId()).thenReturn(Arrays.asList(1, 2, 3));

        List<Integer> result = passengerService.getIds();

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
    }

    @Test
    void getIds_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.findAllId()).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                passengerService.getIds()
        );
    }

    @Test
    void getPassengerById_ShouldReturnPassenger() throws DatabaseActionException {
        when(passengerDao.findById(1)).thenReturn(testPassenger);

        Passenger result = passengerService.getPassengerById(1);

        assertEquals("John", result.getName());
    }

    @Test
    void getPassengerById_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.findById(1)).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                passengerService.getPassengerById(1)
        );
    }

    @Test
    void getPassengerBySurname_ShouldReturnPassengerList() throws DatabaseActionException {
        when(passengerDao.findBySurname("Doe")).thenReturn(Arrays.asList(testPassenger));

        List<Passenger> result = passengerService.getPassengerBySurname("Doe");

        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getSurname());
    }

    @Test
    void getPassengerBySurname_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.findBySurname("Doe")).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                passengerService.getPassengerBySurname("Doe")
        );
    }

    @Test
    void updateExistingPassenger_ShouldUpdatePassenger_WhenDataIsValid() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(true);
        doNothing().when(passengerDao).update(any(Passenger.class));

        passengerService.updateExistingPassenger(
                1, "Jane", "Doe", "jane.doe@example.com", "987654321"
        );

        verify(passengerDao).update(any(Passenger.class));
    }

    @Test
    void updateExistingPassenger_ShouldThrowValidationException_WhenPassengerNotExists() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                passengerService.updateExistingPassenger(1, "Jane", "Doe", "jane.doe@example.com", "987654321")
        );
    }

    @Test
    void updateExistingPassenger_ShouldThrowValidationException_WhenEmailIsInvalid() {
        assertThrows(ValidationException.class, () ->
                passengerService.updateExistingPassenger(1, "John", "Doe", "invalid-email",
                        "123456789")
        );
    }

    @Test
    void updateExistingPassenger_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(true);
        doThrow(new DatabaseActionException("DB error")).when(passengerDao).update(any(Passenger.class));

        assertThrows(ServiceException.class, () ->
                passengerService.updateExistingPassenger(1, "Jane", "Doe", "jane.doe@example.com", "987654321")
        );
    }

    @Test
    void deletePassenger_ShouldCallDaoDelete() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(true);
        doNothing().when(passengerDao).delete(1);

        passengerService.deletePassenger(1);

        verify(passengerDao).delete(1);
    }

    @Test
    void deletePassenger_ShouldThrowValidationException_WhenPassengerNotExists() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                passengerService.deletePassenger(1)
        );
    }

    @Test
    void deletePassenger_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(true);
        doThrow(new DatabaseActionException("DB error")).when(passengerDao).delete(1);

        assertThrows(ServiceException.class, () ->
                passengerService.deletePassenger(1)
        );
    }
}