package lot.exceptions.services;

/**
 * General service layer exception thrown when an error occurs during business logic operations.
 * Extends RuntimeException to indicate it's an unchecked exception.
 */
public class ServiceException extends RuntimeException {
    /**
     * Constructs a new ServiceException with the specified detail message and cause.
     *
     * @param message the detail message describing the service error
     * @param cause the underlying cause of the exception
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
