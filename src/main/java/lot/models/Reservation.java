package lot.models;

public class Reservation {
    private int id;
    private int flightId;
    private int passengerId;
    private String seatNumber;
    private Boolean tookPlace;

    public Reservation(int id, int flightId, int passengerId, String seatNumber, Boolean tookPlace) {
        this.id = id;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatNumber = seatNumber;
        this.tookPlace = tookPlace;
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
}
