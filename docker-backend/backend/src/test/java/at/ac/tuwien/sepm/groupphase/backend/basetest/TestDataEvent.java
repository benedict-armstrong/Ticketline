package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TestDataEvent extends TestData, TestDataVenue, TestDataArtist {

    String TEST_EVENT_TITLE = "TestEventTitle";
    String TEST_EVENT_DESCRIPTION = "Testdescription..";
    LocalDate TEST_EVENT_DATE_FUTURE = LocalDate.parse("2022-12-12");
    LocalDate TEST_EVENT_DATE_FUTURE2 = LocalDate.parse("2023-12-12");
    LocalDate TEST_EVENT_DATE_PAST = LocalDate.parse("2000-12-12");
    LocalDateTime TEST_PERFORMANCE_DATE = LocalDateTime.parse("2023-01-01T12:00");
    String TEST_EVENT_PERFORMANCE_TITLE = "TestPerformance";
    String TEST_EVENT_PERFORMANCE_DESCRIPTION = "TestPerformance description";
    Address TEST_EVENT_LOCATION = Address.builder().name("Max Mustermann").lineOne("Teststraße 1").city("Wien").postcode("1010").country("Österreich").eventLocation(true).build();
    Artist TEST_EVENT_ARTIST = Artist.builder().firstName("Max").lastName("Mustermann").build();
    Event.EventType TEST_EVENT_EVENT_TYPE = Event.EventType.CINEMA;
    Event.EventType TEST_EVENT_EVENT_TYPE2 = Event.EventType.CONCERT;
    int TEST_EVENT_DURATION = 100;

    String EVENT_BASE_URI = BASE_URI + "/events";
    String PERFORMANCE_BASE_URI = BASE_URI + "/performances";

    static Performance getPerformance(Artist artist, Venue venue) {
        return Performance.builder()
            .title(TEST_EVENT_TITLE)
            .description(TEST_EVENT_DESCRIPTION)
            .date(TEST_PERFORMANCE_DATE)
            .artist(artist)
            .venue(venue)
            .ticketTypes(TestDataTicket.getTicketTypes())
            .build();
    }

    static PerformanceDto getPerformanceDto(ArtistDto artistDto, VenueDto venueDto) {
        return PerformanceDto.builder()
            .title(TEST_EVENT_TITLE)
            .description(TEST_EVENT_DESCRIPTION)
            .date(TEST_PERFORMANCE_DATE)
            .artist(artistDto)
            .venue(venueDto)
            .ticketTypes(TestDataTicket.getTicketTypeDtos())
            .build();
    }

    static EventDto getEventDto() {
        return EventDto.builder()
            .name("Test event")
            .description("Test event desc")
            .eventType(TEST_EVENT_EVENT_TYPE)
            .startDate(TEST_EVENT_DATE_FUTURE)
            .endDate(TEST_EVENT_DATE_FUTURE2)
            .duration(TEST_EVENT_DURATION)
            .build();
    }

}
