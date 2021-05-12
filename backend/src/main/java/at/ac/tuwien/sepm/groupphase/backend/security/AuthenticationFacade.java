package at.ac.tuwien.sepm.groupphase.backend.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();
}