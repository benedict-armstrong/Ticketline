package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthorizationException extends AuthenticationException {

    public AuthorizationException(final String msg) {
        super(msg);
    }

}
