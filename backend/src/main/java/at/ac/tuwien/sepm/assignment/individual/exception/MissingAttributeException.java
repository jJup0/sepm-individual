package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * Exception for when an attribute is missing when attempting to add or edit an entity.
 */
public class MissingAttributeException extends Exception{
    public MissingAttributeException() { super(); }
    public MissingAttributeException(String message) { super(message); }
    public MissingAttributeException(Throwable cause) { super(cause); }
    public MissingAttributeException(String message, Throwable cause) { super(message, cause); }
}
