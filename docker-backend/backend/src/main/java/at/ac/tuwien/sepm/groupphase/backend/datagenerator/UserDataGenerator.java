package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

@Profile("generateData")
@Component
public class UserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_USER_TO_GENERATE = 1000;
    private static final String TEST_USER_NAME = "user";
    private static final String TEST_ADDRESS_NAME = "Address";
    private static final String TEST_ADDRESS_CITY = "Vienna";
    private static final String TEST_ADDRESS_POSTCODE = "1000";
    private static final String TEST_ADDRESS_COUNTRY = "Austria";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void generateUser() {

        if (userRepository.findAll().size() >= NUMBER_OF_USER_TO_GENERATE) {
            LOGGER.debug("user already generated");
        } else {
            LOGGER.debug("generating {} user entries", NUMBER_OF_USER_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_USER_TO_GENERATE; i++) {
                ApplicationUser user = ApplicationUser.builder()
                    .email(TEST_USER_NAME + i + "@mail.com")
                    .password("password")
                    .firstName(TEST_USER_NAME + i + "first")
                    .lastName(TEST_USER_NAME + i + "last")
                    .role(ApplicationUser.UserRole.CLIENT)
                    .telephoneNumber("012345" + i)
                    .status(ApplicationUser.UserStatus.ACTIVE)
                    .address(Address.builder()
                        .name(TEST_ADDRESS_NAME + i)
                        .lineOne("line " + i)
                        .city(TEST_ADDRESS_CITY)
                        .postcode(TEST_ADDRESS_POSTCODE)
                        .country(TEST_ADDRESS_COUNTRY)
                        .eventLocation(false)
                        .build())
                    .lastLogin(LocalDateTime.now())
                    .points(0L)
                    .build();

                user.setLastLogin(LocalDateTime.now());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                LOGGER.debug("saving user {}", user);
                userRepository.save(user);
            }
        }
    }

    @PostConstruct
    private void generateAdminUser() {
        if (userRepository.findUserByEmail("admin@mail.com") != null) {
            LOGGER.debug("admin user already generated");
        } else {
            LOGGER.debug("generating admin user");
            ApplicationUser user = ApplicationUser.builder()
                .email("admin@mail.com")
                .password("password")
                .firstName(TEST_USER_NAME + "Admin " + "first")
                .lastName(TEST_USER_NAME + "Admin" + "last")
                .role(ApplicationUser.UserRole.ADMIN)
                .telephoneNumber("0123459")
                .status(ApplicationUser.UserStatus.ACTIVE)
                .address(Address.builder()
                    .name(TEST_ADDRESS_NAME + "Admin")
                    .lineOne("line " + "Admin")
                    .city(TEST_ADDRESS_CITY)
                    .postcode(TEST_ADDRESS_POSTCODE)
                    .country(TEST_ADDRESS_COUNTRY)
                    .eventLocation(false).build())
                .lastLogin(LocalDateTime.now())
                .points(0L)
                .build();

            user.setLastLogin(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            LOGGER.debug("saving admin user {}", user);
            userRepository.save(user);
        }
    }

    @PostConstruct
    private void generateOrganizerUser() {
        if (userRepository.findUserByEmail("organizer@mail.com") != null) {
            LOGGER.debug("organizer user already generated");
        } else {
            LOGGER.debug("generating organizer user");
            ApplicationUser user = ApplicationUser.builder()
                .email("organizer@mail.com")
                .password("password")
                .firstName(TEST_USER_NAME + "Organizer" + "first")
                .lastName(TEST_USER_NAME + "Organizer" + "last")
                .role(ApplicationUser.UserRole.ORGANIZER)
                .telephoneNumber("0123459")
                .status(ApplicationUser.UserStatus.ACTIVE)
                .address(Address.builder()
                    .name(TEST_ADDRESS_NAME + "Organizer")
                    .lineOne("line " + "Organizer")
                    .city(TEST_ADDRESS_CITY)
                    .postcode(TEST_ADDRESS_POSTCODE)
                    .country(TEST_ADDRESS_COUNTRY)
                    .eventLocation(false).build())
                .lastLogin(LocalDateTime.now())
                .points(0L)
                .build();

            user.setLastLogin(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            LOGGER.debug("saving organizer user {}", user);
            userRepository.save(user);
        }
    }
}
