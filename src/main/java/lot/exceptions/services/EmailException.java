package lot.exceptions.services;

/**
 * Exception thrown when an error occurs during email operations.
 * Extends RuntimeException to indicate it's an unchecked exception.
 */
public class EmailException extends RuntimeException {
    /**
     * Constructs a new EmailException with the specified detail message and cause.
     *
     * @param message the detail message describing the email error
     * @param cause the underlying cause of the exception
     */
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
