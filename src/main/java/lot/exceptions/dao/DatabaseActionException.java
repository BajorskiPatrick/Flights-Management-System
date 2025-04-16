package lot.exceptions.dao;

/**
 * Exception thrown when an error occurs during database operations.
 * Extends Exception to indicate it's a checked exception that must be handled.
 */
public class DatabaseActionException extends Exception {
    /**
     * Constructs a new DatabaseActionException with the specified detail message.
     *
     * @param message the detail message describing the database error
     */
    public DatabaseActionException(String message) {super(message);}

    /**
     * Constructs a new DatabaseActionException with the specified detail message and cause.
     *
     * @param message the detail message describing the database error
     * @param cause the underlying cause of the exception
     */
    public DatabaseActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
