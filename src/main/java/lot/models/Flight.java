package lot.models;

import java.time.LocalDateTime;

/**
 * Represents a flight with its details including departure, destination, and timing information.
 */
public class Flight {
    private int id;
    private String departure;
    private String destination;
    private LocalDateTime departureDate;
    private int duration;
    private int seatRowsAmount;

    /**
     * Constructs a Flight object with data retrieved from a database.
     *
     * @param id the flight's unique identifier
     * @param departure the flight's departure location
     * @param destination the flight's destination location
     * @param departureDate the flight's departure date and time
     * @param duration the flight's duration in minutes
     * @param seatRowsAmount the number of seat rows in the flight
     */
    public Flight(int id, String departure, String destination, LocalDateTime departureDate, int duration, int seatRowsAmount) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.duration = duration;
        this.seatRowsAmount = seatRowsAmount;
    }

    /**
     * Constructs a Flight object with data received from the user.
     *
     * @param departure the flight's departure location
     * @param destination the flight's destination location
     * @param departureDate the flight's departure date and time
     * @param duration the flight's duration in minutes
     * @param seatRowsAmount the number of seat rows in the flight
     */
    public Flight(String departure, String destination, LocalDateTime departureDate, int duration, int seatRowsAmount) {
        this.id = -1;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.duration = duration;
        this.seatRowsAmount = seatRowsAmount;
    }

    /**
     * Returns the flight's unique identifier.
     *
     * @return the flight's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the flight's unique identifier.
     *
     * @param id the flight's ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the flight's departure location.
     *
     * @return the departure location
     */
    public String getDeparture() {
        return departure;
    }

    /**
     * Sets the flight's departure location.
     *
     * @param departure the departure location to set
     */
    public void setDeparture(String departure) {
        this.departure = departure;
    }

    /**
     * Returns the flight's destination location.
     *
     * @return the destination location
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the flight's destination location.
     *
     * @param destination the destination location to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Returns the flight's departure date and time.
     *
     * @return the departure date and time
     */
    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    /**
     * Sets the flight's departure date and time.
     *
     * @param departureDate the departure date and time to set
     */
    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * Returns the flight's duration in minutes.
     *
     * @return the flight duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the flight's duration in minutes.
     *
     * @param duration the flight duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Returns the number of seat rows in the flight.
     *
     * @return the number of seat rows
     */
    public int getSeatRowsAmount() {
        return seatRowsAmount;
    }

    /**
     * Sets the number of seat rows in the flight.
     *
     * @param seatRowsAmount the number of seat rows to set
     */
    public void setSeatRowsAmount(int seatRowsAmount) {
        this.seatRowsAmount = seatRowsAmount;
    }

    /**
     * Returns a string representation of the flight.
     *
     * @return a formatted string with reservation details
     */
    @Override
    public String toString() {
        return "- Duration: " + duration + " minutes" + "\n" +
                "- Departure: " + departure + "\n" +
                "- Destination: " + destination + "\n";
    }
}
