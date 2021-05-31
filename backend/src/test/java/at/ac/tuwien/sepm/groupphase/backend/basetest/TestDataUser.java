package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.time.LocalDateTime;

public interface TestDataUser extends TestData{

    String USER_BASE_URI = BASE_URI + "/users";

    String ADMIN_FIRST_NAME = "Max";
    String ADMIN_LAST_NAME = "Mustermann";
    String ADMIN_PHONE_NUMBER = "+43 660 123456789";
    String ADMIN_EMAIL = "admin@email.com";
    String ADMIN_PASSWORD = "password";
    LocalDateTime ADMIN_LAST_LOGIN = LocalDateTime.now();
    int ADMIN_POINTS = 0;
    ApplicationUser.UserStatus ADMIN_USER_STATUS = ApplicationUser.UserStatus.ACTIVE;
    ApplicationUser.UserRole ADMIN_ROLE = ApplicationUser.UserRole.ADMIN;
    Address ADMIN_ADDRESS = Address.builder().name("Max Mustermann").lineOne("Teststraße 1").city("Wien").postcode("1010").country("Österreich").build();

    String DEFAULT_FIRST_NAME = "Max";
    String DEFAULT_LAST_NAME = "Mustermann";
    String DEFAULT_PHONE_NUMBER = "+43 660 123456789";
    String DEFAULT_EMAIL = "user@email.com";
    String DEFAULT_PASSWORD = "password";
    LocalDateTime DEFAULT_LAST_LOGIN = LocalDateTime.now();
    int DEFAULT_POINTS = 0;
    ApplicationUser.UserStatus DEFAULT_STATUS = ApplicationUser.UserStatus.ACTIVE;
    ApplicationUser.UserRole DEFAULT_ROLE = ApplicationUser.UserRole.CLIENT;
    Address DEFAULT_ADDRESS = Address.builder().name("Max Mustermann").lineOne("Teststraße 1").city("Wien").postcode("1010").country("Österreich").build();

    static ApplicationUser getAdmin() {
        return ApplicationUser.builder()
            .firstName(ADMIN_FIRST_NAME)
            .lastName(ADMIN_LAST_NAME)
            .email(ADMIN_EMAIL)
            .lastLogin(ADMIN_LAST_LOGIN)
            .role(ADMIN_ROLE)
            .status(ADMIN_USER_STATUS)
            .password(ADMIN_PASSWORD)
            .points(ADMIN_POINTS)
            .address(TestDataAddress.getAddress())
            .telephoneNumber(ADMIN_PHONE_NUMBER)
            .build();
    }

    static ApplicationUser getUser() {
        return ApplicationUser.builder()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .lastLogin(DEFAULT_LAST_LOGIN)
            .role(DEFAULT_ROLE)
            .status(ApplicationUser.UserStatus.ACTIVE)
            .password(DEFAULT_PASSWORD)
            .points(DEFAULT_POINTS)
            .address(TestDataAddress.getAddress())
            .telephoneNumber(DEFAULT_PHONE_NUMBER)
            .build();
    }

}
