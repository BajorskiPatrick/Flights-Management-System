package lot.services;

import lot.dao.FlightDao;
import lot.dao.PassengerDao;
import lot.dao.ReservationDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.EmailException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Reservation;
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
class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private FlightDao flightDao;

    @Mock
    private PassengerDao passengerDao;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        testReservation = new Reservation(1, 1, "1A");
        testReservation.setId(1);
    }

    @Test
    void makeNewReservation_ShouldReturnReservationId_WhenDataIsValid() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        when(passengerDao.existsById(1)).thenReturn(true);
        when(flightDao.getAvailableSeatsNumbers(1)).thenReturn(Arrays.asList("1A", "1B"));
        when(reservationDao.save(any(Reservation.class))).thenReturn(1);

        int result = reservationService.makeNewReservation(1, 1, "1A");

        assertEquals(1, result);
        verify(reservationDao).save(any(Reservation.class));
    }

    @Test
    void makeNewReservation_ShouldThrowValidationException_WhenFlightNotExists() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.makeNewReservation(1, 1, "1A")
        );
    }

    @Test
    void makeNewReservation_ShouldThrowValidationException_WhenPassengerNotExists() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        when(passengerDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.makeNewReservation(1, 1, "1A")
        );
    }

    @Test
    void makeNewReservation_ShouldThrowValidationException_WhenSeatNotAvailable() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        when(passengerDao.existsById(1)).thenReturn(true);
        when(flightDao.getAvailableSeatsNumbers(1)).thenReturn(Arrays.asList("1B", "1C"));

        assertThrows(ValidationException.class, () ->
                reservationService.makeNewReservation(1, 1, "1A")
        );
    }

    @Test
    void makeNewReservation_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        when(passengerDao.existsById(1)).thenReturn(true);
        when(flightDao.getAvailableSeatsNumbers(1)).thenReturn(Arrays.asList("1A", "1B"));
        when(reservationDao.save(any(Reservation.class))).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.makeNewReservation(1, 1, "1A")
        );
    }

    @Test
    void getAllReservations_ShouldReturnReservationList() throws DatabaseActionException {
        when(reservationDao.findAll()).thenReturn(Arrays.asList(testReservation));

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getFlightId());
    }

    @Test
    void getAllReservations_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(reservationDao.findAll()).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getAllReservations()
        );
    }

    @Test
    void getIds_ShouldReturnIdList() throws DatabaseActionException {
        when(reservationDao.findAllId()).thenReturn(Arrays.asList(1, 2, 3));

        List<Integer> result = reservationService.getIds();

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
    }

    @Test
    void getIds_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(reservationDao.findAllId()).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getIds()
        );
    }

    @Test
    void getFlightIds_ShouldReturnIdList() throws DatabaseActionException {
        when(flightDao.findAllId()).thenReturn(Arrays.asList(1, 2, 3));

        List<Integer> result = reservationService.getFlightIds();

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
    }

    @Test
    void getFlightIds_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.findAllId()).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getFlightIds()
        );
    }

    @Test
    void getPassengerIds_ShouldReturnIdList() throws DatabaseActionException {
        when(passengerDao.findAllId()).thenReturn(Arrays.asList(1, 2, 3));

        List<Integer> result = reservationService.getPassengerIds();

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
    }

    @Test
    void getPassengerIds_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.findAllId()).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getPassengerIds()
        );
    }

    @Test
    void getReservationById_ShouldReturnReservation() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        when(reservationDao.findById(1)).thenReturn(testReservation);

        Reservation result = reservationService.getReservationById(1);

        assertEquals(1, result.getId());
    }

    @Test
    void getReservationById_ShouldThrowValidationException_WhenReservationNotExists() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.getReservationById(1)
        );
    }

    @Test
    void getReservationById_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        when(reservationDao.findById(1)).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getReservationById(1)
        );
    }

    @Test
    void getReservationsByFlightId_ShouldReturnReservationList() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        when(reservationDao.findAllByForeignKey("flights", 1)).thenReturn(Arrays.asList(testReservation));

        List<Reservation> result = reservationService.getReservationsByFlightId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getFlightId());
    }

    @Test
    void getReservationsByFlightId_ShouldThrowValidationException_WhenFlightNotExists() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.getReservationsByFlightId(1)
        );
    }

    @Test
    void getReservationsByFlightId_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        when(reservationDao.findAllByForeignKey("flights", 1)).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getReservationsByFlightId(1)
        );
    }

    @Test
    void getReservationsByPassengerId_ShouldReturnReservationList() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(true);
        when(reservationDao.findAllByForeignKey("passengers", 1)).thenReturn(Arrays.asList(testReservation));

        List<Reservation> result = reservationService.getReservationsByPassengerId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPassengerId());
    }

    @Test
    void getReservationsByPassengerId_ShouldThrowValidationException_WhenPassengerNotExists() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.getReservationsByPassengerId(1)
        );
    }

    @Test
    void getReservationsByPassengerId_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(passengerDao.existsById(1)).thenReturn(true);
        when(reservationDao.findAllByForeignKey("passengers", 1)).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getReservationsByPassengerId(1)
        );
    }

    @Test
    void getReservationBySurname_ShouldReturnReservationList() throws DatabaseActionException {
        when(reservationDao.findAllBySurname("Doe")).thenReturn(Arrays.asList(testReservation));

        List<Reservation> result = reservationService.getReservationBySurname("Doe");

        assertEquals(1, result.size());
    }

    @Test
    void getReservationBySurname_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(reservationDao.findAllBySurname("Doe")).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getReservationBySurname("Doe")
        );
    }

    @Test
    void updateExistingReservation_ShouldUpdateReservation_WhenDataIsValid() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        when(flightDao.existsById(1)).thenReturn(true);
        when(passengerDao.existsById(1)).thenReturn(true);
        when(flightDao.getAvailableSeatsNumbers(1)).thenReturn(Arrays.asList("1A", "1B"));
        when(reservationDao.findById(1)).thenReturn(testReservation);
        doNothing().when(reservationDao).update(any(Reservation.class));

        reservationService.updateExistingReservation(1, 1, 1, "1B");

        verify(reservationDao).update(any(Reservation.class));
    }

    @Test
    void updateExistingReservation_ShouldThrowValidationException_WhenReservationNotExists() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.updateExistingReservation(1, 1, 1, "1A")
        );
    }

    @Test
    void updateExistingReservation_ShouldThrowValidationException_WhenFlightNotExists() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        when(flightDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.updateExistingReservation(1, 1, 1, "1A")
        );
    }

    @Test
    void updateExistingReservation_ShouldThrowValidationException_WhenPassengerNotExists() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        when(flightDao.existsById(1)).thenReturn(true);
        when(passengerDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.updateExistingReservation(1, 1, 1, "1A")
        );
    }

    @Test
    void updateExistingReservation_ShouldThrowValidationException_WhenSeatNotAvailable() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        when(flightDao.existsById(1)).thenReturn(true);
        when(passengerDao.existsById(1)).thenReturn(true);
        when(flightDao.getAvailableSeatsNumbers(1)).thenReturn(Arrays.asList("1B", "1C"));
        when(reservationDao.findById(1)).thenReturn(testReservation);

        assertThrows(ValidationException.class, () ->
                reservationService.updateExistingReservation(1, 1, 1, "1D")
        );
    }

    @Test
    void updateExistingReservation_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        when(flightDao.existsById(1)).thenReturn(true);
        when(passengerDao.existsById(1)).thenReturn(true);
        when(flightDao.getAvailableSeatsNumbers(1)).thenReturn(Arrays.asList("1A", "1B"));
        when(reservationDao.findById(1)).thenReturn(testReservation);
        doThrow(new DatabaseActionException("DB error")).when(reservationDao).update(any(Reservation.class));

        assertThrows(ServiceException.class, () ->
                reservationService.updateExistingReservation(1, 1, 1, "1B")
        );
    }

    @Test
    void deleteReservation_ShouldCallDaoDelete() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        doNothing().when(reservationDao).delete(1);

        reservationService.deleteReservation(1);

        verify(reservationDao).delete(1);
    }

    @Test
    void deleteReservation_ShouldThrowValidationException_WhenReservationNotExists() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                reservationService.deleteReservation(1)
        );
    }

    @Test
    void deleteReservation_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(reservationDao.existsById(1)).thenReturn(true);
        doThrow(new DatabaseActionException("DB error")).when(reservationDao).delete(1);

        assertThrows(ServiceException.class, () ->
                reservationService.deleteReservation(1)
        );
    }

    @Test
    void getAvailableSeats_ShouldReturnSeatList() throws DatabaseActionException {
        when(flightDao.getAvailableSeatsNumbers(1)).thenReturn(Arrays.asList("1A", "1B"));

        List<String> result = reservationService.getAvailableSeats(1);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("1A", "1B")));
    }

    @Test
    void getAvailableSeats_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.getAvailableSeatsNumbers(1)).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                reservationService.getAvailableSeats(1)
        );
    }

    @Test
    void sendEmail_ShouldThrowEmailException_WhenReservationNotFound() throws DatabaseActionException {
        when(reservationDao.findById(1)).thenThrow(new DatabaseActionException("Not found"));

        assertThrows(EmailException.class, () ->
                reservationService.sendEmail(1)
        );
    }

    @Test
    void sendEmail_ShouldThrowEmailException_WhenPassengerNotFound() throws DatabaseActionException {
        when(reservationDao.findById(1)).thenReturn(testReservation);
        when(passengerDao.findById(1)).thenThrow(new DatabaseActionException("Not found"));

        assertThrows(EmailException.class, () ->
                reservationService.sendEmail(1)
        );
    }
}