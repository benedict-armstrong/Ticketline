package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class PdfException extends AuthenticationException {

    public PdfException(final String msg) {
        super(msg);
    }

}
