package lot.models;

public class Reservation {
    private int id;
    private int flightId;
    private int passengerId;
    private String seatNumber;
    private Boolean tookPlace = false;

    /**
     * Constructor used to create Reservation object from data retrieved from a database
     * @param id reservation's id number
     * @param flightId id of the flight to which this reservation is assigned
     * @param passengerId id of the passenger whose this reservation is
     * @param seatNumber number of the seat assigned to this reservation
     */
    public Reservation(int id, int flightId, int passengerId, String seatNumber) {
        this.id = id;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatNumber = seatNumber;
    }

    /**
     * Constructor used to create Reservation object from data received from the user.
     * This object will be later used to
     * @param flightId id of the flight to which this reservation is assigned
     * @param passengerId id of the passenger whose this reservation is
     * @param seatNumber number of the seat assigned to this reservation
     */
    public Reservation(int flightId, int passengerId, String seatNumber) {
        this.id = -1;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", flightId=" + flightId +
                ", passengerId=" + passengerId +
                ", seatNumber='" + seatNumber + '\'' +
                ", tookPlace=" + tookPlace +
                '}';
    }
}
