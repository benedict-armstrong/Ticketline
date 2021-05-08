package at.ac.tuwien.sepm.groupphase.backend.exception;

public class InvalidQueryParameterException extends RuntimeException {

    public InvalidQueryParameterException(String message) {
        super(message);
    }

    public InvalidQueryParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidQueryParameterException(Exception e) {
        super(e);
    }

}
