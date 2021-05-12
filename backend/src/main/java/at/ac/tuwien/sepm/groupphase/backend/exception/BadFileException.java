package at.ac.tuwien.sepm.groupphase.backend.exception;

public class BadFileException extends RuntimeException {

    public BadFileException(String message) {
        super(message);
    }

    public BadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadFileException(Exception e) {
        super(e);
    }

}
