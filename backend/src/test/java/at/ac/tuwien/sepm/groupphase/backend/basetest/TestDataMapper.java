package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface TestDataMapper extends TestData, TestDataAddress, TestDataNews, TestDataArtist, TestDataEvent, TestDataFile, TestDataUser {
    Address ADDRESS_ENTITY = Address.builder()
        .name(TEST_ADDRESS_NAME)
        .lineOne(TEST_ADDRESS_LINE_ONE)
        .city(TEST_ADDRESS_CITY)
        .postcode(TEST_ADDRESS_POSTCODE)
        .country(TEST_ADDRESS_COUNTRY)
        .build();

    AddressDto ADDRESS_DTO = AddressDto.builder()
        .name(TEST_ADDRESS_NAME)
        .lineOne(TEST_ADDRESS_LINE_ONE)
        .city(TEST_ADDRESS_CITY)
        .postcode(TEST_ADDRESS_POSTCODE)
        .country(TEST_ADDRESS_COUNTRY)
        .build();

    Address ADDRESS2_ENTITY = Address.builder()
        .name(TEST_ADDRESS_NAME)
        .lineOne(TEST_ADDRESS_LINE_ONE)
        .city(TEST_ADDRESS_CITY2)
        .postcode(TEST_ADDRESS_POSTCODE)
        .country(TEST_ADDRESS_COUNTRY)
        .build();

    AddressDto ADDRESS2_DTO = AddressDto.builder()
        .name(TEST_ADDRESS_NAME)
        .lineOne(TEST_ADDRESS_LINE_ONE)
        .city(TEST_ADDRESS_CITY2)
        .postcode(TEST_ADDRESS_POSTCODE)
        .country(TEST_ADDRESS_COUNTRY)
        .build();

    Artist ARTIST_ENTITY = Artist.builder()
        .firstName(TEST_ARTIST_FIRSTNAME)
        .lastName(TEST_ARTIST_LASTNAME)
        .build();

    ArtistDto ARTIST_DTO = ArtistDto.builder()
        .firstName(TEST_ARTIST_FIRSTNAME)
        .lastName(TEST_ARTIST_LASTNAME)
        .build();

    Artist ARTIST2_ENTITY = Artist.builder()
        .firstName(TEST_ARTIST_FIRSTNAME2)
        .lastName(TEST_ARTIST_LASTNAME)
        .build();

    ArtistDto ARTIST2_DTO = ArtistDto.builder()
        .firstName(TEST_ARTIST_FIRSTNAME2)
        .lastName(TEST_ARTIST_LASTNAME)
        .build();

    Event EVENT_ENTITY = Event.builder()
        .name(TestDataEvent.TEST_EVENT_TITLE)
        .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
        .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
        .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
        .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
        .duration(TestDataEvent.TEST_EVENT_DURATION)
        .build();

    EventDto EVENT_DTO = EventDto.builder()
        .name(TestDataEvent.TEST_EVENT_TITLE)
        .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
        .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
        .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
        .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
        .duration(TestDataEvent.TEST_EVENT_DURATION)
        .build();

    Event EVENT2_ENTITY = Event.builder()
        .name(TestDataEvent.TEST_EVENT_TITLE)
        .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
        .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
        .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
        .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
        .duration(TestDataEvent.TEST_EVENT_DURATION)
        .build();

    EventDto EVENT2_DTO = EventDto.builder()
        .name(TestDataEvent.TEST_EVENT_TITLE)
        .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
        .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
        .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
        .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
        .duration(TestDataEvent.TEST_EVENT_DURATION)
        .build();

    News NEWS_ENTITY = News.builder()
        .title(TestDataNews.TEST_NEWS_TITLE)
        .text(TestDataNews.TEST_NEWS_TEXT)
        .author("Testuser")
        .event(EVENT_ENTITY)
        .publishedAt(TEST_NEWS_PUBLISHED_AT)
        .build();

    NewsDto NEWS_DTO = NewsDto.builder()
        .title(TestDataNews.TEST_NEWS_TITLE)
        .text(TestDataNews.TEST_NEWS_TEXT)
        .author("Testuser")
        .event(EVENT_DTO.getId())
        .publishedAt(TEST_NEWS_PUBLISHED_AT)
        .build();


    Performance PERFORMANCE_ENTITY = Performance.builder()
        .title(TestDataEvent.TEST_EVENT_TITLE)
        .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
        .date(TestDataEvent.TEST_PERFORMANCE_DATE)
        .artist(ARTIST_ENTITY)
        .venue(
            Venue.builder().build()
        )
        .build();

    PerformanceDto PERFORMANCE_DTO = PerformanceDto.builder()
        .title(TestDataEvent.TEST_EVENT_TITLE)
        .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
        .date(TestDataEvent.TEST_PERFORMANCE_DATE)
        .artist(ARTIST_DTO)
        .venue(
            VenueDto.builder().build()
        )
        .build();

    ApplicationUser USER_ENTITY = ApplicationUser.builder()
        .firstName(ADMIN_FIRST_NAME)
        .lastName(ADMIN_LAST_NAME)
        .email(ADMIN_EMAIL)
        .lastLogin(ADMIN_LAST_LOGIN)
        .role(ADMIN_ROLE)
        .status(ADMIN_USER_STATUS)
        .password(ADMIN_PASSWORD)
        .points(ADMIN_POINTS)
        .address(ADDRESS_ENTITY)
        .telephoneNumber(ADMIN_PHONE_NUMBER)
        .build();

    UserDto USER_DTO = UserDto.builder()
        .firstName(ADMIN_FIRST_NAME)
        .lastName(ADMIN_LAST_NAME)
        .email(ADMIN_EMAIL)
        .lastLogin(ADMIN_LAST_LOGIN)
        .role(ADMIN_ROLE)
        .status(ADMIN_USER_STATUS)
        .password(ADMIN_PASSWORD)
        .points(ADMIN_POINTS)
        .address(ADDRESS_DTO)
        .telephoneNumber(ADMIN_PHONE_NUMBER)
        .build();
}
