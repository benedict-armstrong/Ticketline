package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface TestDataEvent extends TestData {

    Long ID = 1L;
    String TEST_EVENT_TITLE = "TestEventTitle";
    String TEST_EVENT_DESCRIPTION = "Testdescription..";
    LocalDateTime TEST_EVENT_DATE_FUTURE = LocalDateTime.parse("2022-12-12T22:00:00");
    LocalDateTime TEST_EVENT_DATE_FUTURE2 = LocalDateTime.parse("2023-12-12T12:00:00");
    LocalDateTime TEST_EVENT_DATE_PAST = LocalDateTime.parse("2000-12-12T12:00:00");
    String TEST_EVENT_PERFORMANCE_TITLE = "TestPerformance";
    String TEST_EVENT_PERFORMANCE_DESCRIPTION = "TestPerformance description";
    Address TEST_EVENT_LOCATION = Address.builder().name("Max Mustermann").lineOne("Teststraße 1").city("Wien").postcode("1010").country("Österreich").eventLocation(true).build();
    Artist TEST_EVENT_ARTIST = Artist.builder().firstName("Max").lastName("Mustermann").build();
    Event.EventType TEST_EVENT_EVENT_TYPE = Event.EventType.CINEMA;
    Event.EventType TEST_EVENT_EVENT_TYPE2 = Event.EventType.CONCERT;
    int TEST_EVENT_DURATION = 100;

    String EVENT_BASE_URI = BASE_URI + "/events";
    String PEFORMANCE_BASE_URI = BASE_URI + "/performances";

    static Set<SectorType> getTestEventSectortypes() {
        Set<SectorType> sectorTypes = new HashSet<>();
        sectorTypes.add(SectorType.builder().name("Sector").numberOfTickets(100).build());
        return sectorTypes;
    }
}
