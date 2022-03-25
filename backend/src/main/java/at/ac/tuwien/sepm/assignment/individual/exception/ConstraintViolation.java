package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * Exception for when an illegal attempt at an edit of an entity occurs.
 * An illegal edit is one where any constraint is violated.
 */
public class ConstraintViolation extends Exception{
    public ConstraintViolation() { super(); }
    public ConstraintViolation(String message) { super(message); }
    public ConstraintViolation(Throwable cause) { super(cause); }
    public ConstraintViolation(String message, Throwable cause) { super(message, cause); }
}
