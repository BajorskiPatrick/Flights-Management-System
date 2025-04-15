package lot.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
     * Constructor used to create Reservation object from data retrieved from a database that will be presented to the user
     * @param id reservation's id number
     * @param flightId id of the flight to which this reservation is assigned
     * @param passengerId id of the passenger whose this reservation is
     * @param seatNumber number of the seat assigned to this reservation
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
     * Constructor used to save reservation object to database from data taken fromuser
     * This object will be later used to
     * @param flightId id of the flight to which this reservation is assigned
     * @param passengerId id of the passenger whose this reservation is
     * @param seatNumber number of the seat assigned to this reservation
     */
    public Reservation(int flightId, int passengerId, String seatNumber) {
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatNumber = seatNumber;
    }

    /**
     * Used to update reservation
     * @param id
     * @param flightId
     * @param passengerId
     * @param seatNumber
     */
    public Reservation(int id, int flightId, int passengerId, String seatNumber) {
        this.id = id;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatNumber = seatNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Boolean getTookPlace() {
        return tookPlace;
    }

    public void setTookPlace(Boolean tookPlace) {
        this.tookPlace = tookPlace;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerSurname() {
        return passengerSurname;
    }

    public void setPassengerSurname(String passengerSurname) {
        this.passengerSurname = passengerSurname;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

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
