package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;

import java.time.LocalDateTime;

public interface TestDataUser {
    String ADMIN_FIRST_NAME = "Max";
    String ADMIN_LAST_NAME = "Mustermann";
    String ADMIN_PHONE_NUMBER = "+43 660 123456789";
    String ADMIN_EMAIL = "admin@email.com";
    String ADMIN_PASSWORD = "password";
    LocalDateTime ADMIN_LAST_LOGIN = LocalDateTime.now();
    int ADMIN_POINTS = 0;
    ApplicationUser.UserStatus ADMIN_USER_STATUS = ApplicationUser.UserStatus.ACTIVE;
    ApplicationUser.UserRole ADMIN_ROLE = ApplicationUser.UserRole.ADMIN;
    Address ADMIN_ADDRESS = Address.AddressBuilder.anAddress().withName("Max Mustermann").withLineOne("Teststraße 1").withCity("Wien").withPostcode("1010").withCountry("Österreich").build();

    String DEFAULT_FIRST_NAME = "Max";
    String DEFAULT_LAST_NAME = "Mustermann";
    String DEFAULT_PHONE_NUMBER = "+43 660 123456789";
    String DEFAULT_EMAIL = "admin@email.com";
    String DEFAULT_PASSWORD = "password";
    LocalDateTime DEFAULT_LAST_LOGIN = LocalDateTime.now();
    int DEFAULT_POINTS = 0;
    User.Status DEFAULT_STATUS = User.Status.ACTIVE;
    User.UserRole DEFAULT_ROLE = User.UserRole.CLIENT;
    Address DEFAULT_ADDRESS = Address.AddressBuilder.anAddress().withName("Max Mustermann").withLineOne("Teststraße 1").withCity("Wien").withPostcode("1010").withCountry("Österreich").build();
}
