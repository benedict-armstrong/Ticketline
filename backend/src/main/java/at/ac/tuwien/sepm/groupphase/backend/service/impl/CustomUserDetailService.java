package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.AuthorizationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserAlreadyExistAuthenticationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.SimpleMailService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;
    private final SimpleMailService simpleMailService;


    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade, SimpleMailService simpleMailService) {
        this.userRepository = userRepository;
        this.simpleMailService = simpleMailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load a user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getRole() == ApplicationUser.UserRole.ADMIN) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_ORGANIZER", "ROLE_USER");
            } else if (applicationUser.getRole() == ApplicationUser.UserRole.ORGANIZER) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ORGANIZER", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        ApplicationUser applicationUser = userRepository.findUserByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public ApplicationUser addUser(ApplicationUser user) {
        LOGGER.debug("Add new User to System");

        //throw UserAlreadyExistAuthenticationException if email is already in system
        if (userRepository.findUserByEmail(user.getEmail()) == null) {
            user.setLastLogin(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // If user is not ADMIN and tries to set UserRole (to something other than Client), UserStatus or Points throw AuthorizationException
            if (!authenticationFacade.isAdmin()) {
                if (user.getRole() != null && !Objects.equals(user.getRole(), ApplicationUser.UserRole.CLIENT)) {
                    throw new AuthorizationException("You have to be ADMIN to set USER_ROLE");
                }
                if (user.getStatus() != null && !Objects.equals(user.getStatus(), ApplicationUser.UserStatus.ACTIVE)) {
                    throw new AuthorizationException("You have to be ADMIN to set USER_STATUS");
                }
                if (user.getPoints() != 0) {
                    throw new AuthorizationException("You have to be ADMIN to set Points");
                }

            }

            if (user.getRole() == null) {
                user.setRole(ApplicationUser.UserRole.CLIENT);
            }
            if (user.getStatus() == null) {
                user.setStatus(ApplicationUser.UserStatus.ACTIVE);
            }

            simpleMailService.sendMail(user.getEmail(), "New Account at Ticketline", String.format("Hello %s %s,\n\nwelcome to Ticketline!", user.getFirstName(), user.getLastName()));

            return userRepository.save(user);
        }
        throw new UserAlreadyExistAuthenticationException(String.format("User with email address: %s already exists.", user.getEmail()));
    }

    @Override
    public ApplicationUser updateUser(ApplicationUser user) {
        LOGGER.debug("Update User");
        ApplicationUser oldUser = userRepository.findUserByEmail(user.getEmail());

        if (oldUser == null) {
            throw new NotFoundException(String.format("Could not find the user with the email address %s", user.getEmail()));
        }

        if (!oldUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public ApplicationUser updateLastRead(Long userId, Long lastReadNewsId) {
        LOGGER.trace("updateLastRead({}, {})", userId, lastReadNewsId);

        String email = (String) authenticationFacade.getAuthentication().getPrincipal();
        ApplicationUser user = userRepository.findUserByEmail(email);
        if (!user.getId().equals(userId)) {
            throw new IllegalArgumentException("Attempted to update another user's lastReadNews");
        }

        user.setLastReadNewsId(lastReadNewsId);
        return userRepository.save(user);
    }

    @Override
    public ApplicationUser resetPassword(ApplicationUser user) {
        String newGeneratedPassword = RandomStringUtils.randomAscii(16);
        user.setPassword(newGeneratedPassword);

        ApplicationUser newUser = updateUser(user);

        if (newUser != null) {
            simpleMailService.sendMail(user.getEmail(), "[Ticketline] Password reset", String.format("Hello %s %s,\n\nYour password was changed to '%s' (without ')!"
                + " It can be changed in the 'My Account' Tab, after you logged in.", user.getFirstName(), user.getLastName(), newGeneratedPassword));
        }

        return newUser;
    }

    @Override
    public ApplicationUser findUserById(Long id) {
        LOGGER.debug("Find user by id");
        ApplicationUser applicationUser = userRepository.getOne(id);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the id %d", id));
    }
}
