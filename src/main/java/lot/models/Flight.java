package lot.models;

public class Flight {
    private int id;
    private String source;
    private String destination;
    private int duration;
    private Boolean twoWay;

    public Flight(int id, String source, String destination, int duration, Boolean twoWay) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.duration = duration;
        this.twoWay = twoWay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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
}
