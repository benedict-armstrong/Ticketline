package at.ac.tuwien.sepm.groupphase.backend.exception;

public class NoTicketLeftException extends RuntimeException {

    public NoTicketLeftException(String message) {
        super(message);
    }
}
