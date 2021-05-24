package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;

import java.time.LocalDateTime;

public interface TestDataCart extends TestData {
    String CART_BASE_URI = BASE_URI + "/cart";

    Address CART_ADDRESS = Address.builder()
        .name("Cart Test")
        .lineOne("Teststraße 2")
        .city("Wien")
        .postcode("1010")
        .country("Österreich").build();

    ApplicationUser CART_USER = ApplicationUser.builder()
        .firstName("Cart")
        .lastName("Test")
        .telephoneNumber("+43 660 123456789")
        .email("cartTest@email.com")
        .password("password")
        .lastLogin(LocalDateTime.now())
        .points(0)
        .status(ApplicationUser.UserStatus.ACTIVE)
        .role(ApplicationUser.UserRole.CLIENT)
        .address(CART_ADDRESS)
        .build();

    Address CART_ADDRESS2 = Address.builder()
        .name("Cart Test2")
        .lineOne("Teststraße 2")
        .city("Wien")
        .postcode("1010")
        .country("Österreich").build();

    ApplicationUser CART_USER2 = ApplicationUser.builder()
        .firstName("Cart")
        .lastName("Test2")
        .telephoneNumber("+43 660 123456789")
        .email("cartTest2@email.com")
        .password("password")
        .lastLogin(LocalDateTime.now())
        .points(0)
        .status(ApplicationUser.UserStatus.ACTIVE)
        .role(ApplicationUser.UserRole.CLIENT)
        .address(CART_ADDRESS2)
        .build();

    Long CART_TICKET_ID = 0L;

    LocalDateTime CART_CREATION_DATE = LocalDateTime.now();

    Integer CART_AMOUNT = 3;
}
