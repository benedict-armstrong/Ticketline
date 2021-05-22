package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataFile;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
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

    private Set<File> images = new HashSet<>();

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

        images = new HashSet<>();
        images.add(file);

        performance = Performance.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .artist(artist)
            .location(address)
            .sectorTypes(TestDataEvent.getTestEventSectortypes())
            .images(images)
            .build();

        fileRepository.save(file);

        userRepository.deleteAll();
        saveUser(AUTH_USER_ORGANIZER, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ORGANIZER, mockMvc, objectMapper);
    }

    @Test
    @DisplayName("Should return empty list when getting all news but there are none")
    public void givenNothing_whenGetAllNews_thenReturnEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.PEFORMANCE_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<PerformanceDto> performanceDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), PerformanceDto[].class)
        );
        assertEquals(0, performanceDtos.size());
    }

    @Test
    @DisplayName("Should return correctly sized chunk of events on get all")
    public void givenEvent_whenGetAll_thenGetCorrectChunk() throws Exception {
        int amountOfEventsToGenerate = 15;
        int pageSize = 5;
        for (int i = 0; i < amountOfEventsToGenerate; i++) {
            Performance e = Performance.builder()
                .title(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .date(TestDataEvent.TEST_EVENT_DATE_FUTURE)
                .artist(artist)
                .location(address)
                .sectorTypes(TestDataEvent.getTestEventSectortypes())
                .build();
            performanceRepository.save(e);
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.PEFORMANCE_BASE_URI)
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
    @DisplayName("Should return 422 on get all with negative parameters")
    public void whenGetAllWithNegativeLimit_then422() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.PEFORMANCE_BASE_URI)
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
            get(TestDataEvent.PEFORMANCE_BASE_URI)
                .param("page", "A")
        ).andReturn();
        MvcResult mvcResultO = this.mockMvc.perform(
            get(TestDataEvent.PEFORMANCE_BASE_URI)
                .param("size", "-")
        ).andReturn();
        MvcResult mvcResultLO = this.mockMvc.perform(
            get(TestDataEvent.PEFORMANCE_BASE_URI)
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
    public void whenCreatePerformance_then201AndPerformanceWithIdAndPublishedAt() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.PEFORMANCE_BASE_URI)
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
    @DisplayName("Should return 400 when Date is in past")
    public void whenCreatePerformanceWithPastDate_then400() throws Exception {
        Performance invalidPerformance = Performance.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_EVENT_DATE_PAST)
            .artist(TestDataEvent.TEST_EVENT_ARTIST)
            .location(TestDataEvent.TEST_EVENT_LOCATION)
            .sectorTypes(TestDataEvent.getTestEventSectortypes())
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.PEFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPerformance))
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
            .date(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .artist(TestDataEvent.TEST_EVENT_ARTIST)
            .location(TestDataEvent.TEST_EVENT_LOCATION)
            .sectorTypes(new HashSet<>())
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.PEFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPerformance))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
