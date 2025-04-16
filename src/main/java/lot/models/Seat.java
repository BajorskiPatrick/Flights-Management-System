package lot.models;

/**
 * Represents a seat on a flight with its availability status.
 */
public class Seat {
    private int flightId;
    private String seatNumber;
    private Boolean isAvailable;

    /**
     * Constructs a Seat object.
     *
     * @param flightId the ID of the flight associated with the seat
     * @param seatNumber the seat number
     * @param isAvailable the availability status of the seat
     */
    public Seat(int flightId, String seatNumber, Boolean isAvailable) {
        this.flightId = flightId;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
    }

    /**
     * Returns the ID of the flight associated with the seat.
     *
     * @return the flight's ID
     */
    public int getFlightId() {
        return flightId;
    }

    /**
     * Sets the ID of the flight associated with the seat.
     *
     * @param flightId the flight's ID to set
     */
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Returns the seat number.
     *
     * @return the seat number
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number.
     *
     * @param seatNumber the seat number to set
     */
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Returns the availability status of the seat.
     *
     * @return true if the seat is available, false otherwise
     */
    public Boolean getAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the seat.
     *
     * @param available true if the seat is available, false otherwise
     */
    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
