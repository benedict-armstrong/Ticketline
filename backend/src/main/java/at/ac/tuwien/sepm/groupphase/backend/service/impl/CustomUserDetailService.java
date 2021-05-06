package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserAlreadyExistAuthenticationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
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

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getRole() == ApplicationUser.UserRole.ADMIN) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
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
        if (userRepository.findUserByEmail(user.getEmail()) == null) {
            if (user.getRole() == null) {
                user.setRole(ApplicationUser.UserRole.CLIENT);
            }
            if (user.getStatus() == null) {
                user.setStatus(ApplicationUser.UserStatus.ACTIVE);
            }
            user.setLastLogin(LocalDateTime.now());

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            return userRepository.save(user);
        }
        throw new UserAlreadyExistAuthenticationException(String.format("User with email address: %s already exists.", user.getEmail()));
    }

    @Override
    public ApplicationUser updateUser(ApplicationUser user) {
        LOGGER.debug("Update User");
        if (userRepository.findUserByEmail(user.getEmail()) != null) {

            user.setPassword(passwordEncoder.encode(user.getPassword())); //TODO: notwendig wegen update password!?

            return userRepository.save(user);
        }

        throw new NotFoundException(String.format("Could not find the user with the email address %s", user.getEmail()));
    }
}
