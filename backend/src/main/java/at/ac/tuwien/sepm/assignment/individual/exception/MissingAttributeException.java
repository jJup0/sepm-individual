package at.ac.tuwien.sepm.assignment.individual.exception;

public class MissingAttributeException extends Exception{
    public MissingAttributeException() { super(); }
    public MissingAttributeException(String message) { super(message); }
    public MissingAttributeException(Throwable cause) { super(cause); }
    public MissingAttributeException(String message, Throwable cause) { super(message, cause); }
}
