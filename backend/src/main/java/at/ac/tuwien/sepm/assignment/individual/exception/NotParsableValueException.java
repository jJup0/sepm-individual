package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * Exception for when an invalid date ist trying to be formatted.
 */
public class NotParsableValueException extends Exception{
    public NotParsableValueException() {
        super();
    }

    public NotParsableValueException(String message) {
        super(message);
    }

    public NotParsableValueException(Throwable cause) {
        super(cause);
    }

    public NotParsableValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
