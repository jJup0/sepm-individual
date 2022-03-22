package at.ac.tuwien.sepm.assignment.individual.exception;

// TODO naming?
public class MyInternalServerError extends Exception {
    public MyInternalServerError() { super(); }
    public MyInternalServerError(String message) { super(message); }
    public MyInternalServerError(Throwable cause) { super(cause); }
    public MyInternalServerError(String message, Throwable cause) { super(message, cause); }
}
