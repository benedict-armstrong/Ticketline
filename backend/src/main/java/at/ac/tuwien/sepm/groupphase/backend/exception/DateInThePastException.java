package at.ac.tuwien.sepm.groupphase.backend.exception;

public class DateInThePastException extends RuntimeException {
    public DateInThePastException(String message) {
        super(message);
    }
}
