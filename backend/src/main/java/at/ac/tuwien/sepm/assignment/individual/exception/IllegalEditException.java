package at.ac.tuwien.sepm.assignment.individual.exception;

public class IllegalEditException extends Exception{
    public IllegalEditException() { super(); }
    public IllegalEditException(String message) { super(message); }
    public IllegalEditException(Throwable cause) { super(cause); }
    public IllegalEditException(String message, Throwable cause) { super(message, cause); }
}
