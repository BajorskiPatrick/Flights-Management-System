package lot.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a flight reservation with details about the flight, passenger, and seat.
 */
public class Reservation {
    private int id = -1;
    private int flightId;
    private int passengerId;
    private String passengerName = "";
    private String passengerSurname = "";
    private String seatNumber;
    private LocalDateTime departureDate = null;
    private Boolean tookPlace = false;

    /**
     * Constructs a Reservation object with data retrieved from a database for presentation.
     *
     * @param id the reservation's unique identifier
     * @param flightId the ID of the flight associated with the reservation
     * @param passengerId the ID of the passenger associated with the reservation
     * @param passengerName the name of the passenger associated with the reservation
     * @param passengerSurname the surname of the passenger associated with the reservation
     * @param seatNumber the seat number assigned to the reservation
     * @param departureDate the departure date and time of the flight
     */
    public Reservation(int id, int flightId, int passengerId, String passengerName, String passengerSurname, String seatNumber, LocalDateTime departureDate) {
        this.id = id;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.passengerSurname = passengerSurname;
        this.seatNumber = seatNumber;
        this.departureDate = departureDate;
    }

    /**
     * Constructs a Reservation object with data received from the user for database storage.
     *
     * @param flightId the ID of the flight associated with the reservation
     * @param passengerId the ID of the passenger associated with the reservation
     * @param seatNumber the seat number assigned to the reservation
     */
    public Reservation(int flightId, int passengerId, String seatNumber) {
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatNumber = seatNumber;
    }

    /**
     * Constructs a Reservation object for updating an existing reservation.
     *
     * @param id the reservation's unique identifier
     * @param flightId the ID of the flight associated with the reservation
     * @param passengerId the ID of the passenger associated with the reservation
     * @param seatNumber the seat number assigned to the reservation
     */
    public Reservation(int id, int flightId, int passengerId, String seatNumber) {
        this.id = id;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatNumber = seatNumber;
    }

    /**
     * Returns the reservation's unique identifier.
     *
     * @return the reservation's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the reservation's unique identifier.
     *
     * @param id the reservation's unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the flight associated with the reservation.
     *
     * @return the flight's ID
     */
    public int getFlightId() {
        return flightId;
    }

    /**
     * Sets the ID of the flight associated with the reservation.
     *
     * @param flightId the flight's ID to set
     */
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Returns the ID of the passenger associated with the reservation.
     *
     * @return the passenger's ID
     */
    public int getPassengerId() {
        return passengerId;
    }

    /**
     * Sets the ID of the passenger associated with the reservation.
     *
     * @param passengerId the passenger's ID to set
     */
    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    /**
     * Returns the seat number assigned to the reservation.
     *
     * @return the seat number
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number assigned to the reservation.
     *
     * @param seatNumber the seat number to set
     */
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Returns whether the reservation has taken place.
     *
     * @return true if the reservation has taken place, false otherwise
     */
    public Boolean getTookPlace() {
        return tookPlace;
    }

    /**
     * Sets whether the reservation has taken place.
     *
     * @param tookPlace true if the reservation has taken place, false otherwise
     */
    public void setTookPlace(Boolean tookPlace) {
        this.tookPlace = tookPlace;
    }

    /**
     * Returns the name of the passenger associated with the reservation.
     *
     * @return the passenger's name
     */
    public String getPassengerName() {
        return passengerName;
    }

    /**
     * Sets the name of the passenger associated with the reservation.
     *
     * @param passengerName the passenger's name to set
     */
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    /**
     * Returns the surname of the passenger associated with the reservation.
     *
     * @return the passenger's surname
     */
    public String getPassengerSurname() {
        return passengerSurname;
    }

    /**
     * Sets the surname of the passenger associated with the reservation.
     *
     * @param passengerSurname the passenger's surname to set
     */
    public void setPassengerSurname(String passengerSurname) {
        this.passengerSurname = passengerSurname;
    }

    /**
     * Returns the departure date and time of the flight.
     *
     * @return the departure date and time
     */
    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    /**
     * Sets the departure date and time of the flight.
     *
     * @param departureDate the departure date and time to set
     */
    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * Returns a string representation of the reservation.
     *
     * @return a formatted string with reservation details
     */
    @Override
    public String toString() {
        return "- Reservation's number: " + id + "\n" +
                "- Flight's number: " + flightId + "\n" +
                "- Passenger's number: " + passengerId + "\n" +
                "- Passenger's name: " + passengerName + '\n' +
                "- Passenger's surname: " + passengerSurname + '\n' +
                "- Seat number: " + seatNumber + '\n' +
                "- Departure date and time: " + departureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + '\n';
    }
}
