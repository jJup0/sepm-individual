package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * Exception for when an illegal attempt at an edit of an entity occurs.
 * An illegal edit is one where any constraint is violated.
 */
public class IllegalEditException extends Exception{
    public IllegalEditException() { super(); }
    public IllegalEditException(String message) { super(message); }
    public IllegalEditException(Throwable cause) { super(cause); }
    public IllegalEditException(String message, Throwable cause) { super(message, cause); }
}
