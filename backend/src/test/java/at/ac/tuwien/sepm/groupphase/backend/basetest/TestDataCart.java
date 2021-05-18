package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface TestDataCart extends TestData {
    String CART_BASE_URI = BASE_URI + "/carts";

    Address CART_ADDRESS = Address.AddressBuilder.anAddress()
        .withName("Cart Test")
        .withLineOne("Teststraße 2")
        .withCity("Wien")
        .withPostcode("1010")
        .withCountry("Österreich").build();

    ApplicationUser CART_USER = ApplicationUser.UserBuilder.anUser()
        .withFirstName("Cart")
        .withLastName("Test")
        .withTelephoneNumber("+43 660 123456789")
        .withEmail("cartTest@email.com")
        .withPassword("password")
        .withLastLogin(LocalDateTime.now())
        .withPoints(0)
        .withStatus(ApplicationUser.UserStatus.ACTIVE)
        .withRole(ApplicationUser.UserRole.CLIENT)
        .withAddress(CART_ADDRESS)
        .build();

    SectorType CART_SECTOR_TYPE = SectorType.SectorTypeBuilder.aSectorType().withName("Normal").withNumberOfTickets(10).build();

    static Set<SectorType> getTestCartSectorTypes() {
        Set<SectorType> sectorTypes = new HashSet<>();
        sectorTypes.add(CART_SECTOR_TYPE);
        return sectorTypes;
    }

    Event CART_EVENT = Event.EventBuilder.aEvent()
        .withEventType(Event.EventType.CINEMA)
        .withArtist(Artist.ArtistBuilder.anArtist().withFirstName("Artist").withLastName("Artisto").build())
        .withDate(LocalDateTime.now())
        .withDescription("CartTest Event")
        .withDuration(5)
        .withLocation(CART_ADDRESS)
        .withSectorTypes(getTestCartSectorTypes())
        .withTitle("CART TEST EVENT")
        .build();

    Integer CART_AMOUNT = 3;
}
