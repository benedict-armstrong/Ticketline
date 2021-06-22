package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address.
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on their id.
     *
     * @param id the id
     * @return a application user
     */
    ApplicationUser findApplicationUserById(long id);

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * Add a user to system.
     *
     * @param user to add
     * @return added User
     */
    ApplicationUser addUser(ApplicationUser user);

    /**
     * Update a user.
     *
     * @param user to update
     * @return updated User
     */
    ApplicationUser updateUser(ApplicationUser user, Boolean firstAuthentication);

    /**
     * Marks that a user read a particular news.
     *
     * @param userId the ID of the user.
     * @param lastReadNewsId the ID of the last read news.
     * @return the updated user.
     */
    ApplicationUser updateLastRead(Long userId, Long lastReadNewsId);

    /**
     * Updates User with new password and sends it per mail.
     *
     * @param user to update
     * @return updated User
     */
    ApplicationUser resetPassword(ApplicationUser user);

    /**
     * Retrieves a list of all users.
     *
     * @param pageRequest the page and size to be retrieved.
     * @return the list of users on this page.
     */
    List<ApplicationUser> getAll(Pageable pageRequest);

    /**
     * Reset the password attempt count of all users.
     *
     */
    void resetPasswordAttemptCount();

}
