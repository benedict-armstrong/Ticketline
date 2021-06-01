package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataVenue;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EventEndpointTest implements TestDataEvent, TestAuthentification {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    private String authToken;

    private EventDto eventDto;

    private ArtistDto artistDto;

    private VenueDto venueDto;

    @BeforeEach
    public void beforeEach() throws Exception {
        artistDto = TestDataArtist.getArtistDto();
        venueDto = TestDataVenue.getVenueDto();

        PerformanceDto[] performanceDtos = new PerformanceDto[1];
        performanceDtos[0] = PerformanceDto.builder()
            .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
            .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE)
            .ticketTypes(TestDataTicket.getTicketTypeDtos())
            .build();

        eventDto = EventDto.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .performances(performanceDtos)
            .build();

        saveUser(AUTH_USER_ORGANIZER, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ORGANIZER, mockMvc, objectMapper);
    }

    @AfterEach
    public void afterEach() {
        eventRepository.deleteAll();
        performanceRepository.deleteAll();
        venueRepository.deleteAll();
        userRepository.deleteAll();
        artistRepository.deleteAll();
        addressRepository.deleteAll();
    }

    private void saveEvent(EventDto eventDto) throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(BASE_URI + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    }

    private ArtistDto saveArtist(ArtistDto artistDto) throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(ARTIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(artistDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        return objectMapper.readValue(response.getContentAsString(),
            ArtistDto.class);
    }

    private VenueDto saveVenue(VenueDto venueDto) throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        return objectMapper.readValue(response.getContentAsString(),
            VenueDto.class);
    }

    @Test
    @DisplayName("Should return 201 and news object with set ID")
    public void whenCreateEvent_then201AndEventWithIdAndPublishedAt() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        EventDto savedDto = objectMapper.readValue(response.getContentAsString(), EventDto.class);
        assertAll(
            () -> assertNotNull(savedDto.getId())
        );
    }

    @Test
    @DisplayName("Should return 400 when start Date is in past")
    public void whenCreateEventWithPastStartDate_then400() throws Exception {
        PerformanceDto[] performanceDtos = new PerformanceDto[1];
        performanceDtos[0] = PerformanceDto.builder()
            .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
            .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE)
            .artist(saveArtist(artistDto))
            .ticketTypes(TestDataTicket.getTicketTypeDtos())
            .venue(saveVenue(venueDto))
            .build();

        eventDto = EventDto.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_PAST)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .performances(performanceDtos)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 400 when end Date is in past")
    public void whenCreateEventWithPastEndDate_then400() throws Exception {
        PerformanceDto[] performanceDtos = new PerformanceDto[1];
        performanceDtos[0] = PerformanceDto.builder()
            .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
            .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE)
            .artist(saveArtist(artistDto))
            .ticketTypes(TestDataTicket.getTicketTypeDtos())
            .venue(saveVenue(venueDto))
            .build();

        eventDto = EventDto.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_PAST)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .performances(performanceDtos)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 400 when date of performance is outside of event period")
    public void whenCreateEventWithPerformanceDateOutOfPeriod_then400() throws Exception {
        PerformanceDto[] performanceDtos = new PerformanceDto[1];
        performanceDtos[0] = PerformanceDto.builder()
            .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
            .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(TestDataEvent.TEST_EVENT_DATE_FUTURE2.plusWeeks(1).atStartOfDay())
            .artist(saveArtist(artistDto))
            .ticketTypes(TestDataTicket.getTicketTypeDtos())
            .venue(saveVenue(venueDto))
            .build();

        eventDto = EventDto.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .performances(performanceDtos)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return correctly sized chunk of events on get all")
    public void givenEvent_whenGetAll_thenGetCorrectChunk() throws Exception {
        int amountOfEventsToGenerate = 6;
        int pageSize = 5;
        for (int i = 0; i < amountOfEventsToGenerate; i++) {
            PerformanceDto[] performanceDtos = new PerformanceDto[1];
            performanceDtos[0] = PerformanceDto.builder()
                .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
                .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
                .date(TestDataEvent.TEST_PERFORMANCE_DATE)
                .artist(saveArtist(artistDto))
                .ticketTypes(TestDataTicket.getTicketTypeDtos())
                .venue(saveVenue(venueDto))
                .build();

            eventDto = EventDto.builder()
                .name(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
                .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
                .duration(TestDataEvent.TEST_EVENT_DURATION)
                .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
                .performances(performanceDtos)
                .build();

            saveEvent(eventDto);
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(pageSize))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        EventDto[] eventDtos = objectMapper.readValue(response.getContentAsString(), EventDto[].class);
        assertEquals(pageSize, eventDtos.length);
    }

    @Test
    @DisplayName("Should return empty list when getting all news but there are none")
    public void givenNothing_whenGetAllEvents_thenReturnEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );
        assertEquals(0, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 422 on get all with negative parameters")
    public void whenGetAllWithNegativeLimit_then422() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "-1")
                .param("size", "-2")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 422 on get all with non-numeric parameters")
    public void whenGetAllWithNonNumericParams_then422() throws Exception {
        MvcResult mvcResultL = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "A")
        ).andReturn();
        MvcResult mvcResultO = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("size", "-")
        ).andReturn();
        MvcResult mvcResultLO = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "~")
                .param("size", "B")
        ).andReturn();

        MockHttpServletResponse responseL = mvcResultL.getResponse();
        MockHttpServletResponse responseO = mvcResultO.getResponse();
        MockHttpServletResponse responseLO = mvcResultLO.getResponse();
        assertAll(
            () -> assertEquals(HttpStatus.BAD_REQUEST.value(), responseL.getStatus()),
            () -> assertEquals(HttpStatus.BAD_REQUEST.value(), responseO.getStatus()),
            () -> assertEquals(HttpStatus.BAD_REQUEST.value(), responseLO.getStatus())
        );
    }

    @Test
    @DisplayName("Should return 200 and all events when no search params are given")
    public void whenEventsGiven_SearchWithNoParams_ShouldReturn200AndEvents() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));
        saveEvent(eventDto);

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event with title from search")
    public void whenEventsGiven_SearchWithTitle_ShouldReturn200AndEvent() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));
        saveEvent(eventDto);

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("title", TestDataEvent.TEST_EVENT_TITLE)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(eventDto.getName(), eventDtos.get(0).getName());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event with duration when duration minus 30 is given")
    public void whenEventsGiven_SearchWithDurationMinus30_ShouldReturn200AndEvent() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));
        saveEvent(eventDto);

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("duration", Integer.toString(TestDataEvent.TEST_EVENT_DURATION - 30))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(eventDto.getDuration(), eventDtos.get(0).getDuration());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event description when word is in description")
    public void whenEventsGiven_SearchWithDescription_ShouldReturn200AndEvent() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));
        saveEvent(eventDto);

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("description", TestDataEvent.TEST_EVENT_DESCRIPTION.substring(0,4))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(eventDto.getDescription(), eventDtos.get(0).getDescription());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event with event type")
    public void whenEventsGiven_SearchWithEventType_ShouldReturn200AndEvent() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));
        saveEvent(eventDto);

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("eventType", TestDataEvent.TEST_EVENT_EVENT_TYPE.toString())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(eventDto.getEventType(), eventDtos.get(0).getEventType());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event with all search params")
    public void whenEventsGiven_SearchWithAllParams_ShouldReturn200AndEvent() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));
        saveEvent(eventDto);

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("title", TestDataEvent.TEST_EVENT_TITLE)
                .param("description", TestDataEvent.TEST_EVENT_DESCRIPTION)
                .param("duration", Integer.toString(TestDataEvent.TEST_EVENT_DURATION))
                .param("eventType", TestDataEvent.TEST_EVENT_EVENT_TYPE.toString())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(eventDto.getName(), eventDtos.get(0).getName());
        assertEquals(eventDto.getDescription(), eventDtos.get(0).getDescription());
        assertEquals(eventDto.getDuration(), eventDtos.get(0).getDuration());
        assertEquals(eventDto.getEventType(), eventDtos.get(0).getEventType());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return nothing when event given and search with other event type")
    public void whenEventsGiven_SearchWithOtherEventType_ShouldReturnNothing() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));
        saveEvent(eventDto);

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("eventType", TestDataEvent.TEST_EVENT_EVENT_TYPE2.toString())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );

        assertEquals(0, eventDtos.size());
    }
}
