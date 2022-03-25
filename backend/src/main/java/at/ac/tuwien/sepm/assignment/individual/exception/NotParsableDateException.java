package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * Exception for when an invalid date ist trying to be formatted.
 */
public class NotParsableDateException extends Exception{
    public NotParsableDateException() {
        super();
    }

    public NotParsableDateException(String message) {
        super(message);
    }

    public NotParsableDateException(Throwable cause) {
        super(cause);
    }

    public NotParsableDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
