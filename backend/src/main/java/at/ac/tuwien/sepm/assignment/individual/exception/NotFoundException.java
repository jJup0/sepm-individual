package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * Exception for when an entity that is trying to be accessed is not found.
 */
public class NotFoundException extends Exception {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
