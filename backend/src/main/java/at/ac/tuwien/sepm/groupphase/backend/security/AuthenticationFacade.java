package at.ac.tuwien.sepm.groupphase.backend.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

    /**
     * Get Authentication Object for Current Request.
     *
     * @return currently authenticated User as Authentication Object (Anonymous if no user is logged in)
     */
    Authentication getAuthentication();

    /**
     * Returns true if the User making the request is Authenticated as an Admin.
     *
     * @return true if the User is Admin false otherwise
     */
    boolean isAdmin();

    /**
     * Returns true if the User making the request is Authenticated as an Organizer.
     *
     * @return true if the User is Organizer false otherwise
     */
    boolean isOrganizer();

    /**
     * Returns true if the User making the request is Authenticated as a User.
     *
     * @return true if the User is authenticated and not Anonymous false otherwise
     */
    boolean isUser();

}