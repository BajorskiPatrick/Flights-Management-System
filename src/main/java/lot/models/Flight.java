package lot.models;

import java.time.LocalDateTime;

public class Flight {
    private int id;
    private String departure;
    private String destination;
    private LocalDateTime departureDate;
    private int duration;
    private Boolean twoWay;

    /**
     * Constructor used to create Flight object from data retrieved from a database
     * @param id flight's id number
     * @param departure flight's sour
     * @param destination flight's destination
     * @param departureDate flight's departure date
     * @param duration flight's duration
     * @param twoWay boolean value indicating that this flight is a one-way (false) or a two-way (true) flight
     */
    public Flight(int id, String departure, String destination, LocalDateTime departureDate, int duration, Boolean twoWay) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.duration = duration;
        this.twoWay = twoWay;
    }

    /**
     * Constructor used to create Flight object from data received from the user
     * @param departure flight's sour
     * @param destination flight's destination
     * @param departureDate flight's departure date
     * @param duration flight's duration
     * @param twoWay boolean value indicating that this flight is a one-way (false) or a two-way (true) flight
     */
    public Flight(String departure, String destination, LocalDateTime departureDate, int duration, Boolean twoWay) {
        this.id = -1;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.duration = duration;
        this.twoWay = twoWay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Boolean getTwoWay() {
        return twoWay;
    }

    public void setTwoWay(Boolean twoWay) {
        this.twoWay = twoWay;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", departureDate=" + departureDate +
                ", duration=" + duration +
                ", twoWay=" + twoWay +
                '}';
    }
}
