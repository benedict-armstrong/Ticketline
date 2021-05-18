package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface TestDataEvent extends TestData {

    Long ID = 1L;
    String TEST_EVENT_TITLE = "TestEventTitle";
    String TEST_EVENT_DESCRIPTION = "Testdescription..";
    LocalDateTime TEST_EVENT_DATE_FUTURE = LocalDateTime.parse("2022-12-12T22:00:00");
    LocalDateTime TEST_EVENT_DATE_PAST = LocalDateTime.parse("2000-12-12T12:00:00");
    Event.EventType TEST_EVENT_EVENT_TYPE = Event.EventType.CINEMA;
    int TEST_EVENT_DURATION = 100;


    String EVENT_BASE_URI = BASE_URI + "/events";

    static Set<SectorType> getTestEventSectortypes() {
        Set<SectorType> sectorTypes = new HashSet<>();
        sectorTypes.add(SectorType.SectorTypeBuilder.aSectorType().withName("Sector").withNumberOfTickets(100).build());
        return sectorTypes;
    }

    static Artist getTestEventArtist() {
        return Artist.ArtistBuilder.anArtist().withFirstName("Max").withLastName("Mustermann").build();
    }

    static Address getTestEventLocation() {
        return Address.AddressBuilder.anAddress().withName("Max Mustermann").withLineOne("Teststraße 1").withCity("Wien").withPostcode("1010").withCountry("Österreich").build();
    }
}
