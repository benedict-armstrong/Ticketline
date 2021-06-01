package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataFile;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PerformanceEndpointTest implements TestDataEvent, TestAuthentification {

    @Autowired
    private MockMvc mockMvc;

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

    private String authToken;

    private Performance performance;

    @BeforeEach
    public void beforeEach() throws Exception {
        performanceRepository.deleteAll();
        fileRepository.deleteAll();
        addressRepository.deleteAll();
        artistRepository.deleteAll();

        address = addressRepository.save(TestDataEvent.TEST_EVENT_LOCATION);
        artist = artistRepository.save(TestDataEvent.TEST_EVENT_ARTIST);

        performance = Performance.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE)
            .artist(artist)
            .ticketTypes(TestDataTicket.getTicketTypes())
            .build();

        fileRepository.save(file);

        userRepository.deleteAll();
        saveUser(AUTH_USER_ORGANIZER, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ORGANIZER, mockMvc, objectMapper);
    }

    @AfterEach
    public void afterEach() {
        performanceRepository.deleteAll();
        fileRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 201 and performance object with set ID")
    public void whenCreatePerformance_then201AndPerformanceWithId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.PERFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(performance))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PerformanceDto savedDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);
        assertAll(
            () -> assertNotNull(savedDto.getId())
        );
    }

    @Test
    @DisplayName("Should return 200 and performance with the given ID on get by ID")
    public void givenPerformance_whenGetOneById_then200AndPerformanceWithId() throws Exception {
        performanceRepository.save(performance);

        assertNotNull(performance);

        MvcResult mvcResult = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI + "/" + performance.getId())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PerformanceDto performanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

        assertEquals(performanceDto.getId(), performance.getId());
    }


    @Test
    @DisplayName("Should return correctly sized chunk of performances on get all")
    public void givenPerformance_whenGetAll_thenGetCorrectChunk() throws Exception {
        int amountOfPerformancesToGenerate = 15;
        int pageSize = 5;

        for (int i = 0; i < amountOfPerformancesToGenerate; i++) {
            Performance performance = Performance.builder()
                .title(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .date(TestDataEvent.TEST_PERFORMANCE_DATE)
                .artist(artist)
                .ticketTypes(TestDataTicket.getTicketTypes())
                .build();

            performanceRepository.save(performance);
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI)
                .param("page", String.valueOf(1))
                .param("size", String.valueOf(pageSize))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PerformanceDto[] performanceDtos = objectMapper.readValue(response.getContentAsString(), PerformanceDto[].class);
        assertEquals(pageSize, performanceDtos.length);
    }

    @Test
    @DisplayName("Should return correctly sized chunk of performances on get by artist")
    public void givenPerformance_whenGetByArtist_thenGetCorrectChunk() throws Exception {
        int amountOfPerformancesToGenerate = 15;
        int pageSize = 5;

        artistRepository.save(TestDataEvent.TEST_EVENT_ARTIST);
        addressRepository.save(TestDataEvent.TEST_EVENT_LOCATION);

        for (int i = 0; i < amountOfPerformancesToGenerate; i++) {
            Performance performance = Performance.builder()
                .title(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .date(TestDataEvent.TEST_PERFORMANCE_DATE)
                .artist(artist)
                .ticketTypes(TestDataTicket.getTicketTypes())
                .build();

            performanceRepository.save(performance);
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI)
                .param("artistId", artist.getId().toString())
                .param("page", String.valueOf(1))
                .param("size", String.valueOf(pageSize))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PerformanceDto[] performanceDtos = objectMapper.readValue(response.getContentAsString(), PerformanceDto[].class);
        assertEquals(pageSize, performanceDtos.length);
    }

    @Test
    @DisplayName("Should return correctly sized chunk of performances on get by location")
    public void givenPerformance_whenGetByLocation_thenGetCorrectChunk() throws Exception {
        int amountOfPerformancesToGenerate = 15;
        int pageSize = 5;

        artistRepository.save(TestDataEvent.TEST_EVENT_ARTIST);
        addressRepository.save(TestDataEvent.TEST_EVENT_LOCATION);

        for (int i = 0; i < amountOfPerformancesToGenerate; i++) {
            Performance performance = Performance.builder()
                .title(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .date(TestDataEvent.TEST_PERFORMANCE_DATE)
                .artist(artist)
                .ticketTypes(TestDataTicket.getTicketTypes())
                .build();

            performanceRepository.save(performance);
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI)
                .param("addressId", address.getId().toString())
                .param("page", String.valueOf(1))
                .param("size", String.valueOf(pageSize))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PerformanceDto[] performanceDtos = objectMapper.readValue(response.getContentAsString(), PerformanceDto[].class);
        assertEquals(pageSize, performanceDtos.length);
    }

    @Test
    @DisplayName("Should return 400 when Date is in past")
    public void whenCreatePerformanceWithPastDate_then400() throws Exception {
        Performance invalidPerformance = Performance.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE.minusYears(10))
            .artist(TestDataEvent.TEST_EVENT_ARTIST)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.PERFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPerformance))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 400 when no sectortypes")
    public void whenCreatePerformanceWithNoSectorTypes_then400() throws Exception {
        Performance invalidPerformance = Performance.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE)
            .artist(TestDataEvent.TEST_EVENT_ARTIST)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.PERFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPerformance))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
