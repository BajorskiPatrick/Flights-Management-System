package lot.services;

import lot.dao.FlightDao;
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightDao flightDao;

    @InjectMocks
    private FlightService flightService;

    private Flight testFlight;

    @BeforeEach
    void setUp() {
        testFlight = new Flight("Warsaw", "London", LocalDateTime.now().plusDays(1), 120, 10);
        testFlight.setId(1);
    }

    @Test
    void addNewFlight_ShouldReturnFlightId_WhenDataIsValid() throws DatabaseActionException {
        when(flightDao.save(any(Flight.class))).thenReturn(1);

        int result = flightService.addNewFlight("Warsaw", "London",
                LocalDate.now().plusDays(1), "12:00", 120, 10);

        assertEquals(1, result);
        verify(flightDao).save(any(Flight.class));
    }

    @Test
    void addNewFlight_ShouldThrowValidationException_WhenDateIsPast() {
        assertThrows(ValidationException.class, () ->
                flightService.addNewFlight(
                        "Warsaw", "London", LocalDate.now().minusDays(1), "12:00",
                        120, 10)
        );
    }

    @Test
    void addNewFlight_ShouldThrowValidationException_WhenTimeIsInvalid() {
        assertThrows(ValidationException.class, () ->
                flightService.addNewFlight(
                        "Warsaw", "London", LocalDate.now().plusDays(1), "25:00",
                        120, 10)
        );
    }

    @Test
    void addNewFlight_ShouldThrowValidationException_WhenDurationIsInvalid() {
        assertThrows(ValidationException.class, () ->
                flightService.addNewFlight(
                        "Warsaw", "London", LocalDate.now().plusDays(1), "12:00",
                        -10, 10)
        );
    }

    @Test
    void addNewFlight_ShouldThrowValidationException_WhenSeatRowsIsInvalid() {
        assertThrows(ValidationException.class, () ->
                flightService.addNewFlight(
                        "Warsaw", "London", LocalDate.now().plusDays(1), "12:00",
                        120, 0)
        );
    }

    @Test
    void addNewFlight_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.save(any(Flight.class))).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                flightService.addNewFlight(
                        "Warsaw", "London", LocalDate.now().plusDays(1), "12:00",
                        120, 10)
        );
    }

    @Test
    void getAllFlights_ShouldReturnFlightList() throws DatabaseActionException {
        when(flightDao.findAll()).thenReturn(Arrays.asList(testFlight));

        List<Flight> result = flightService.getAllFlights();

        assertEquals(1, result.size());
        assertEquals("Warsaw", result.get(0).getDeparture());
    }

    @Test
    void getAllFlights_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.findAll()).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                flightService.getAllFlights()
        );
    }

    @Test
    void getFlightById_ShouldReturnFlight() throws DatabaseActionException {
        when(flightDao.findById(1)).thenReturn(testFlight);
        when(flightDao.existsById(1)).thenReturn(true);

        Flight result = flightService.getFlightById(1);

        assertEquals("Warsaw", result.getDeparture());
    }

    @Test
    void getFlightById_ShouldThrowValidationException_WhenFlightNotExists() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                flightService.getFlightById(1)
        );
    }

    @Test
    void getFlightById_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        when(flightDao.findById(1)).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                flightService.getFlightById(1)
        );
    }

    @Test
    void getFlightByDeparture_ShouldReturnFlightList() throws DatabaseActionException {
        when(flightDao.findByDeparture("Warsaw")).thenReturn(Arrays.asList(testFlight));

        List<Flight> result = flightService.getFlightByDeparture("Warsaw");

        assertEquals(1, result.size());
        assertEquals("Warsaw", result.get(0).getDeparture());
    }

    @Test
    void getFlightByDeparture_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.findByDeparture("Warsaw")).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                flightService.getFlightByDeparture("Warsaw")
        );
    }

    @Test
    void getFlightByDestination_ShouldReturnFlightList() throws DatabaseActionException {
        when(flightDao.findByDestination("London")).thenReturn(Arrays.asList(testFlight));

        List<Flight> result = flightService.getFlightByDestination("London");

        assertEquals(1, result.size());
        assertEquals("London", result.get(0).getDestination());
    }

    @Test
    void getFlightByDestination_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.findByDestination("London")).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                flightService.getFlightByDestination("London")
        );
    }

    @Test
    void getFlightByDate_ShouldReturnFlightList() throws DatabaseActionException {
        LocalDate date = LocalDate.now().plusDays(1);
        when(flightDao.findByDate(date)).thenReturn(Arrays.asList(testFlight));

        List<Flight> result = flightService.getFlightByDate(date);

        assertEquals(1, result.size());
        assertEquals(date, result.get(0).getDepartureDate().toLocalDate());
    }

    @Test
    void getFlightByDate_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        LocalDate date = LocalDate.now().plusDays(1);
        when(flightDao.findByDate(date)).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                flightService.getFlightByDate(date)
        );
    }

    @Test
    void getIds_ShouldReturnIdList() throws DatabaseActionException {
        when(flightDao.findAllId()).thenReturn(Arrays.asList(1, 2, 3));

        List<Integer> result = flightService.getIds();

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
    }

    @Test
    void getIds_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.findAllId()).thenThrow(new DatabaseActionException("DB error"));

        assertThrows(ServiceException.class, () ->
                flightService.getIds()
        );
    }

    @Test
    void updateExistingFlight_ShouldUpdateFlight_WhenDataIsValid() throws DatabaseActionException {
        when(flightDao.findById(1)).thenReturn(testFlight);
        when(flightDao.existsById(1)).thenReturn(true);

        flightService.updateExistingFlight(1, "Berlin", "Paris",
                LocalDate.now().plusDays(2), "14:00", 90, 10);

        verify(flightDao).update(any(Flight.class));
    }

    @Test
    void updateExistingFlight_ShouldThrowValidationException_WhenSeatRowsDecreased() throws DatabaseActionException {
        when(flightDao.findById(1)).thenReturn(testFlight);
        when(flightDao.existsById(1)).thenReturn(true);

        assertThrows(ValidationException.class, () ->
                flightService.updateExistingFlight(1, "Berlin", "Paris",
                        LocalDate.now().plusDays(2), "14:00", 90, 5)
        );
    }

    @Test
    void updateExistingFlight_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.findById(1)).thenReturn(testFlight);
        when(flightDao.existsById(1)).thenReturn(true);
        doThrow(new DatabaseActionException("DB error")).when(flightDao).update(any(Flight.class));

        assertThrows(ServiceException.class, () ->
                flightService.updateExistingFlight(1, "Berlin", "Paris",
                        LocalDate.now().plusDays(2), "14:00", 90, 10)
        );
    }

    @Test
    void deleteFlight_ShouldCallDaoDelete() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        doNothing().when(flightDao).delete(1);

        flightService.deleteFlight(1);

        verify(flightDao).delete(1);
    }

    @Test
    void deleteFlight_ShouldThrowValidationException_WhenFlightNotExists() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(false);

        assertThrows(ValidationException.class, () ->
                flightService.deleteFlight(1)
        );
    }

    @Test
    void deleteFlight_ShouldThrowServiceException_WhenDatabaseFails() throws DatabaseActionException {
        when(flightDao.existsById(1)).thenReturn(true);
        doThrow(new DatabaseActionException("DB error")).when(flightDao).delete(1);

        assertThrows(ServiceException.class, () ->
                flightService.deleteFlight(1)
        );
    }
}