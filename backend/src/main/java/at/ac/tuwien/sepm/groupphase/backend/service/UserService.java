package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    ApplicationUser updateUser(ApplicationUser user);

    /**
     * Updates User with new password and sends it per mail.
     *
     * @param user to update
     * @return updated User
     */
    ApplicationUser resetPassword(ApplicationUser user);


    /**
     * Find a user based on the id.
     *
     * @param id the user id
     * @return a application user
     */
    ApplicationUser findUserById(Long id);

}
