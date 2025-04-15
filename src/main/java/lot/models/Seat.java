package lot.models;

public class Seat {
    private int flightId;
    private String seatNumber;
    private Boolean isAvailable;

    public Seat(int flightId, String seatNumber, Boolean isAvailable) {
        this.flightId = flightId;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
