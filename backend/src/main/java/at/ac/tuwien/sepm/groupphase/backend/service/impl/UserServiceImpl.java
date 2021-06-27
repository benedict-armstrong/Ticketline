package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.PasswordResetToken;
import at.ac.tuwien.sepm.groupphase.backend.exception.AuthorizationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserAlreadyExistAuthenticationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.PasswordTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.SimpleMailService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordTokenService passwordTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;
    private final SimpleMailService simpleMailService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordTokenService passwordTokenService, PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade, SimpleMailService simpleMailService) {
        this.userRepository = userRepository;
        this.passwordTokenService = passwordTokenService;
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
    public ApplicationUser findApplicationUserById(long id) {
        LOGGER.debug("Find application user by id");
        ApplicationUser manager = userRepository.findUserByEmail(authenticationFacade.getAuthentication().getPrincipal().toString());
        if (authenticationFacade.isAdmin() || manager.getId() == id) {
            ApplicationUser applicationUser = userRepository.findUserById(id);
            if (applicationUser != null) {
                return applicationUser;
            } else {
                throw new NotFoundException(String.format("Could not find the user with the id %d", id));
            }
        } else {
            throw new AuthorizationException("You don't have the authorization the view this user");
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
    public ApplicationUser updateUser(ApplicationUser user, Boolean firstAuthentication) {
        LOGGER.debug("Update User");
        if (!firstAuthentication) {
            //Stop non allowed users to change users
            ApplicationUser manager = userRepository.findUserByEmail(authenticationFacade.getAuthentication().getPrincipal().toString());
            if (!authenticationFacade.isAdmin() && !(manager.getEmail().equals(user.getEmail()))) {
                throw new AuthorizationException("You don't have the authorization the change this user");
            }
        }

        ApplicationUser oldUser = userRepository.findUserByEmail(user.getEmail());

        if (oldUser == null) {
            throw new NotFoundException(String.format("Could not find the user with the email address %s", user.getEmail()));
        }

        if (!oldUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (oldUser.getRole() == ApplicationUser.UserRole.ADMIN) {
            user.setStatus(ApplicationUser.UserStatus.ACTIVE); // Prevent the admin from banning themself
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
    @Transactional
    public void sendPasswordResetLink(String email) {
        LOGGER.trace("sendPasswordResetLink({})", email);
        ApplicationUser user = findApplicationUserByEmail(email);

        LocalDate expiryDate = LocalDate.now().plusDays(1L);
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = PasswordResetToken.builder().token(token).user(user).expiryDate(expiryDate).build();
        myToken = passwordTokenService.save(myToken);

        //Frontend Domain and port are hardcoded
        simpleMailService.sendMail(user.getEmail(), "[Ticketline] Password reset", String.format("Hello %s %s,\n\nSomeone requested a password reset link. If this wasn't you, you can ignore this mail."
            + "\n\nYou can change your password here: \n"
            + "localhost:4200/changePassword?token=%s (this link is only valid for 24 hours)", user.getFirstName(), user.getLastName(), myToken.getToken()));
    }

    @Override
    @Transactional
    public void changePassword(String password, String token) {
        LOGGER.trace("changePassword({}, {})", password, token);

        ApplicationUser user = passwordTokenService.findUserByToken(token);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public List<ApplicationUser> getAll(Pageable pageRequest) {
        LOGGER.trace("getAll({})", pageRequest);
        return userRepository.findAllByStatusNot(ApplicationUser.UserStatus.DELETED, pageRequest).getContent();
    }

    @Override
    @Scheduled(fixedDelay = 18000)
    public void resetPasswordAttemptCount() {
        LOGGER.trace("resetPasswordAttemptCount()");
        List<ApplicationUser> users = userRepository.findAllByPointsGreaterThan(0L);

        for (ApplicationUser user : users) {
            user.setPoints(0L);
            userRepository.save(user);
        }
    }

    @Override
    public void delete(Long id) {
        LOGGER.trace("delete({})", id);

        ApplicationUser requester = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        if (requester == null) {
            throw new NotFoundException("Couldn't find user with id " + id);
        }

        if (!requester.getId().equals(id)) {
            throw new IllegalArgumentException("Attempted to delete another user");
        } else if (requester.getRole() != ApplicationUser.UserRole.CLIENT) {
            throw new IllegalArgumentException("Attempted to delete a non-client user");
        } else if (requester.getStatus() == ApplicationUser.UserStatus.DELETED) {
            throw new NotFoundException("Couldn't find user with id " + id);
        }
        requester.setFirstName(null);
        requester.setLastName(null);
        requester.setTelephoneNumber(null);
        requester.setEmail(null);
        requester.setPassword(null);
        requester.setLastLogin(null);
        requester.setLastReadNewsId(null);
        requester.setPoints(null);
        requester.setStatus(ApplicationUser.UserStatus.DELETED);
        requester.setAddress(null);
        userRepository.save(requester);
    }
}
