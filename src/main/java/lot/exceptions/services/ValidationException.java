package lot.exceptions.services;

/**
 * Exception thrown when data validation fails.
 * Extends RuntimeException to indicate it's an unchecked exception.
 */
public class ValidationException extends RuntimeException {
    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message describing the validation error
     */
    public ValidationException(String message) {
        super(message);
    }
}
