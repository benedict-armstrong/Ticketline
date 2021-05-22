package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataFile;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;


    private final File file = File.builder()
        .data(TestDataFile.TEST_FILE_DATA)
        .type(TestDataFile.TEST_FILE_TYPE)
        .build();

    private Address address;

    private Artist artist;

    private Set<File> images = new HashSet<>();

    private String authToken;

    private Event event;

    @BeforeEach
    public void beforeEach() throws Exception {
        eventRepository.deleteAll();
        performanceRepository.deleteAll();
        fileRepository.deleteAll();
        addressRepository.deleteAll();
        artistRepository.deleteAll();

        address = addressRepository.save(TestDataEvent.TEST_EVENT_LOCATION);
        artist = artistRepository.save(TestDataEvent.TEST_EVENT_ARTIST);

        images = new HashSet<>();
        images.add(file);

        Set<Performance> performances = new HashSet<>();
        performances.add(Performance.builder()
            .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
            .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .artist(artist)
            .location(address)
            .sectorTypes(TestDataEvent.getTestEventSectortypes())
            .images(images)
            .build());

        event = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .performances(performances)
            .build();

        fileRepository.save(file);

        userRepository.deleteAll();
        saveUser(AUTH_USER_ORGANIZER, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ORGANIZER, mockMvc, objectMapper);
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
    @DisplayName("Should return correctly sized chunk of events on get all")
    public void givenEvent_whenGetAll_thenGetCorrectChunk() throws Exception {
        int amountOfEventsToGenerate = 15;
        int pageSize = 5;
        for (int i = 0; i < amountOfEventsToGenerate; i++) {
            event = Event.builder()
                .name(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
                .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
                .duration(TestDataEvent.TEST_EVENT_DURATION)
                .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
                .build();

            eventRepository.save(event);
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", String.valueOf(1))
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
    @DisplayName("Should return 201 and news object with set ID")
    public void whenCreateEvent_then201AndEventWithIdAndPublishedAt() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
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
        Set<File> images = new HashSet<>();
        images.add(file);
        fileRepository.save(file);

        Set<Performance> performances = new HashSet<>();
        performances.add(Performance.builder()
            .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
            .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .artist(artist)
            .location(address)
            .sectorTypes(TestDataEvent.getTestEventSectortypes())
            .images(images)
            .build());

        Event invalidEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_PAST)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .performances(performances)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEvent))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 400 when end Date is in past")
    public void whenCreateEventWithPastEndDate_then400() throws Exception {
        Set<File> images = new HashSet<>();
        images.add(file);
        fileRepository.save(file);

        Set<Performance> performances = new HashSet<>();
        performances.add(Performance.builder()
            .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
            .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .artist(artist)
            .location(address)
            .sectorTypes(TestDataEvent.getTestEventSectortypes())
            .images(images)
            .build());

        Event invalidEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_PAST)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .performances(performances)
            .build();
        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEvent))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

//    @Test
//    @DisplayName("Should return 403 on trying to create a new event without needed permission")
//    public void whenCreateEvent_withOutPermission_returns403() throws Exception {
//        String invalidAuthToken = authenticate(AUTH_USER_CLIENT, mockMvc, objectMapper);
//        MvcResult mvcResult = this.mockMvc.perform(
//            post(TestDataEvent.EVENT_BASE_URI)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(event))
//                .header(securityProperties.getAuthHeader(), invalidAuthToken)
//        ).andReturn();
//
//        MockHttpServletResponse response = mvcResult.getResponse();
//        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
//        assertEquals("", response.getContentAsString());
//    }

    @Test
    @DisplayName("Should return 200 and all events when no search params are given")
    public void whenEventsGiven_SearchWithNoParams_ShouldReturn200AndEvents() throws Exception {
        Event searchEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();

        eventRepository.save(searchEvent);

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
        Event searchEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();

        eventRepository.save(searchEvent);

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
        assertEquals(searchEvent.getName(), eventDtos.get(0).getName());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event with duration when duration minus 30 is given")
    public void whenEventsGiven_SearchWithDurationMinus30_ShouldReturn200AndEvent() throws Exception {
        Event searchEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();

        eventRepository.save(searchEvent);

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
        assertEquals(searchEvent.getDuration(), eventDtos.get(0).getDuration());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event description when word is in description")
    public void whenEventsGiven_SearchWithDescription_ShouldReturn200AndEvent() throws Exception {
        Event searchEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();

        eventRepository.save(searchEvent);

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
        assertEquals(searchEvent.getDescription(), eventDtos.get(0).getDescription());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event with event type")
    public void whenEventsGiven_SearchWithEventType_ShouldReturn200AndEvent() throws Exception {
        Event searchEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();

        eventRepository.save(searchEvent);

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
        assertEquals(searchEvent.getEventType(), eventDtos.get(0).getEventType());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and event with all search params")
    public void whenEventsGiven_SearchWithAllParams_ShouldReturn200AndEvent() throws Exception {
       Event searchEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();

        eventRepository.save(searchEvent);

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
        assertEquals(searchEvent.getName(), eventDtos.get(0).getName());
        assertEquals(searchEvent.getDescription(), eventDtos.get(0).getDescription());
        assertEquals(searchEvent.getDuration(), eventDtos.get(0).getDuration());
        assertEquals(searchEvent.getEventType(), eventDtos.get(0).getEventType());
        assertEquals(1, eventDtos.size());
    }

    @Test
    @DisplayName("Should return nothing when event given and search with other event type")
    public void whenEventsGiven_SearchWithOtherEventType_ShouldReturnNothing() throws Exception {
        Event searchEvent = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();

        eventRepository.save(searchEvent);

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
