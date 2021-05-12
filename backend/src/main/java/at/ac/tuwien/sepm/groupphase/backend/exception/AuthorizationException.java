package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthorizationExeption extends AuthenticationException {

    public AuthorizationExeption(final String msg) {
        super(msg);
    }

}
