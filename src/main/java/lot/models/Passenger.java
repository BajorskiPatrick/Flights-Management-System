package lot.models;

/**
 * Represents a passenger with personal details.
 */
public class Passenger {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    /**
     * Constructs a Passenger object with data retrieved from a database.
     *
     * @param id the passenger's unique identifier
     * @param name the passenger's first name
     * @param surname the passenger's last name
     * @param email the passenger's email address
     * @param phoneNumber the passenger's phone number
     */
    public Passenger(int id, String name, String surname, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Constructs a Passenger object with data received from the user.
     *
     * @param name the passenger's first name
     * @param surname the passenger's last name
     * @param email the passenger's email address
     * @param phoneNumber the passenger's phone number
     */
    public Passenger(String name, String surname, String email, String phoneNumber) {
        this.id = -1;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the passenger's unique identifier.
     *
     * @return the passenger's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the passenger's unique identifier.
     *
     * @param id the passenger's ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the passenger's first name.
     *
     * @return the passenger's first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the passenger's first name.
     *
     * @param name the first name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the passenger's last name.
     *
     * @return the passenger's last name
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the passenger's last name.
     *
     * @param surname the last name to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the passenger's email address.
     *
     * @return the passenger's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the passenger's email address.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the passenger's phone number.
     *
     * @return the passenger's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the passenger's phone number.
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
