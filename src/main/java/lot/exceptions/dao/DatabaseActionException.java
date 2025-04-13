package lot.exceptions.dao;

public class DatabaseActionException extends Exception {
    public DatabaseActionException(String message) {super(message);}
    public DatabaseActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
