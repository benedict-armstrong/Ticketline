package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface TestDataCart extends TestData {
    String CART_BASE_URI = BASE_URI + "/carts";

    Address CART_ADDRESS = Address.builder()
        .name("Cart Test")
        .lineOne("Teststraße 2")
        .lineTwo("Wien")
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

    SectorType CART_SECTOR_TYPE = SectorType.builder().name("Normal").numberOfTickets(10).build();

    static Set<SectorType> getTestCartSectorTypes() {
        Set<SectorType> sectorTypes = new HashSet<>();
        sectorTypes.add(CART_SECTOR_TYPE);
        return sectorTypes;
    }

    Event CART_EVENT = Event.builder()
        .eventType(Event.EventType.CINEMA)
        .artist(Artist.builder().firstName("Artist").lastName("Artisto").build())
        .date(LocalDateTime.now())
        .description("CartTest Event")
        .duration(5)
        .location(CART_ADDRESS)
        .sectorTypes(getTestCartSectorTypes())
        .title("CART TEST EVENT")
        .build();

    Integer CART_AMOUNT = 3;
}
