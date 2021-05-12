package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

@Profile("generateData")
@Component
public class UserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_USER_TO_GENERATE = 5;
    private static final String TEST_USER_NAME = "User";
    private static final String TEST_ADDRESS_NAME = "Address";
    private static final String TEST_ADDRESS_CITY = "Vienna";
    private static final String TEST_ADDRESS_POSTCODE = "1000";
    private static final String TEST_ADDRESS_COUNTRY = "Austria";

    private final UserRepository userRepository;

    public UserDataGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void generateUser() {

        if (userRepository.findAll().size() > 0) {
            LOGGER.debug("user already generated");
        } else {
            LOGGER.debug("generating {} user entries", NUMBER_OF_USER_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_USER_TO_GENERATE; i++) {
                ApplicationUser user = ApplicationUser.UserBuilder.anUser().withEmail(TEST_USER_NAME + i + "@gmx.com")
                                        .withPassword(TEST_USER_NAME + i).withFirstName(TEST_USER_NAME + i + " first")
                                        .withLastName(TEST_USER_NAME + i + " last").withRole(ApplicationUser.UserRole.CLIENT)
                                        .withTelephoneNumber("012345" + i).withStatus(ApplicationUser.UserStatus.ACTIVE)
                                        .withAddress(Address.AddressBuilder.anAddress().withName(TEST_ADDRESS_NAME + i)
                                        .withLineOne("line " + i).withCity(TEST_ADDRESS_CITY)
                                        .withPostcode(TEST_ADDRESS_POSTCODE).withCountry(TEST_ADDRESS_COUNTRY).build())
                                        .withLastLogin(LocalDateTime.now()).build();
                LOGGER.debug("saving user {}", user);
                userRepository.save(user);
            }
        }
    }

    @PostConstruct
    private void generateAdminUser() {
        if (userRepository.findUserByEmail(TEST_USER_NAME + "Admin" + "@gmx.com") != null) {
            LOGGER.debug("admin user already generated");
        } else {
            LOGGER.debug("generating admin user");
            ApplicationUser user = ApplicationUser.UserBuilder.anUser().withEmail(TEST_USER_NAME + "Admin" + "@gmx.com")
                .withPassword(TEST_USER_NAME + "Admin").withFirstName(TEST_USER_NAME + "Admin" + " first")
                .withLastName(TEST_USER_NAME + "Admin" + " last").withRole(ApplicationUser.UserRole.ADMIN)
                .withTelephoneNumber("0123459").withStatus(ApplicationUser.UserStatus.ACTIVE)
                .withAddress(Address.AddressBuilder.anAddress().withName(TEST_ADDRESS_NAME + "Admin")
                    .withLineOne("line " + "Admin").withCity(TEST_ADDRESS_CITY)
                    .withPostcode(TEST_ADDRESS_POSTCODE).withCountry(TEST_ADDRESS_COUNTRY).build())
                .withLastLogin(LocalDateTime.now()).build();
            LOGGER.debug("saving admin user {}", user);
            userRepository.save(user);

        }
    }

    @PostConstruct
    private void generateOrganizerUser() {
        if (userRepository.findUserByEmail(TEST_USER_NAME + "Organizer" + "@gmx.com") != null) {
            LOGGER.debug("organizer user already generated");
        } else {
            LOGGER.debug("generating organizer user");
            ApplicationUser user = ApplicationUser.UserBuilder.anUser().withEmail(TEST_USER_NAME + "Organizer" + "@gmx.com")
                .withPassword(TEST_USER_NAME + "Organizer").withFirstName(TEST_USER_NAME + "Organizer" + " first")
                .withLastName(TEST_USER_NAME + "Organizer" + " last").withRole(ApplicationUser.UserRole.ORGANIZER)
                .withTelephoneNumber("0123459").withStatus(ApplicationUser.UserStatus.ACTIVE)
                .withAddress(Address.AddressBuilder.anAddress().withName(TEST_ADDRESS_NAME + "Organizer")
                    .withLineOne("line " + "Organizer").withCity(TEST_ADDRESS_CITY)
                    .withPostcode(TEST_ADDRESS_POSTCODE).withCountry(TEST_ADDRESS_COUNTRY).build())
                .withLastLogin(LocalDateTime.now()).build();
            LOGGER.debug("saving organizer user {}", user);
            userRepository.save(user);

        }
    }
}
