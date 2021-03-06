package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

    // Authentication beans

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    private String authToken;

    // Other beans
    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AddressRepository addressRepository;

    private VenueDto venueDto = VenueDto.builder()
        .name(VENUE_NAME)
        .sectors( Arrays.asList(SECTOR_DTO_SEATED, SECTOR_DTO_STAGE, SECTOR_DTO_STANDING))
        .address(
            AddressDto.builder()
                .name(VENUE_ADDRESS_NAME)
                .lineOne(VENUE_ADDRESS_LINE_ONE)
                .city(VENUE_ADDRESS_CITY)
                .postcode(VENUE_ADDRESS_POSTCODE)
                .country(VENUE_ADDRESS_COUNTRY)
                .eventLocation(false)
                .build())
        .layout(VENUE_LAYOUT_DTO)
        .build();
    private ArtistDto artistDto = ArtistDto.builder()
        .firstName("Art")
        .lastName("ist")
        .build();
    private TicketTypeDto[] ticketTypeDtos = new TicketTypeDto[]{
        TicketTypeDto.builder()
            .title("TTD").price(500)
            .build()
    };
    private PerformanceDto performanceDto = PerformanceDto.builder()
        .title("Perf Dto")
        .description("Perf Desc")
        .venue(venueDto)
        .artist(artistDto)
        .ticketTypes(ticketTypeDtos)
        .date(LocalDateTime.now().plusDays(10))
        .build();

    @BeforeEach
    public void beforeEach() throws Exception {
        saveUser(AUTH_USER_ORGANIZER, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ORGANIZER, mockMvc, objectMapper);

        venueDto = VenueDto.builder()
            .name("VenueDto")
            .sectors( Arrays.asList(SECTOR_DTO_SEATED, SECTOR_DTO_STAGE, SECTOR_DTO_STANDING))
            .address(
                AddressDto.builder()
                    .name(VENUE_ADDRESS_NAME)
                    .lineOne(VENUE_ADDRESS_LINE_ONE)
                    .city(VENUE_ADDRESS_CITY)
                    .postcode(VENUE_ADDRESS_POSTCODE)
                    .country(VENUE_ADDRESS_COUNTRY)
                    .eventLocation(false)
                    .build()
                )
            .layout(VENUE_LAYOUT_DTO)
            .build();
        artistDto = ArtistDto.builder()
            .firstName("Art")
            .lastName("ist")
            .build();
        ticketTypeDtos = new TicketTypeDto[]{
            TicketTypeDto.builder()
                .title("TTD").price(500)
                .build()
        };
        performanceDto = PerformanceDto.builder()
            .title("Perf Dto")
            .description("Perf Desc")
            .venue(venueDto)
            .artist(artistDto)
            .ticketTypes(ticketTypeDtos)
            .date(LocalDateTime.now().plusDays(10))
            .build();

        ArtistDto savedArtistDto = saveArtist(artistDto);
        performanceDto.setArtist(savedArtistDto);

        VenueDto savedVenueDto = saveVenue(venueDto);
        performanceDto.setVenue(savedVenueDto);
    }

    @AfterEach
    public void afterEach() {
        performanceRepository.deleteAll();
        venueRepository.deleteAll();
        userRepository.deleteAll();
        artistRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 201 and a DTO with set ID back after creating performance")
    public void whenCreatePerformance_then201AndGetDtoWithIdBack() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(PERFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(performanceDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PerformanceDto savedPerformanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

        assertNotNull(savedPerformanceDto.getId());
    }

    @Test
    @DisplayName("Should return 200 and performance with the given ID on get by ID")
    public void givenPerformance_whenGetOneById_then200AndPerformanceWithId() throws Exception {
        // Create
        MvcResult mvcResult = this.mockMvc.perform(
            post(PERFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(performanceDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        PerformanceDto savedDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

        // Get
        MvcResult mvcResultGet = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI + "/" + savedDto.getId())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse responseGet = mvcResultGet.getResponse();
        assertEquals(HttpStatus.OK.value(), responseGet.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, responseGet.getContentType());

        PerformanceDto performanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

        assertEquals(performanceDto.getId(), savedDto.getId());
    }

    @Test
    @DisplayName("Should return correctly sized chunk of performances on get all")
    public void givenPerformance_whenGetAll_thenGetCorrectChunk() throws Exception {
        int amountOfPerformancesToGenerate = 15;
        int pageSize = 5;

        for (int i = 0; i < amountOfPerformancesToGenerate; i++) {
            VenueDto savedVenue = saveVenue(venueDto);
            ArtistDto savedArtist = saveArtist(artistDto);
            PerformanceDto perf = PerformanceDto.builder()
                .title(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .date(TestDataEvent.TEST_PERFORMANCE_DATE)
                .artist(savedArtist)
                .venue(savedVenue)
                .ticketTypes(TestDataTicket.getTicketTypeDtos())
                .build();
            this.mockMvc.perform(
                post(PERFORMANCE_BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(perf))
                    .header(securityProperties.getAuthHeader(), authToken)
            ).andReturn();
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

        Long anArtistId = null;
        for (int i = 0; i < amountOfPerformancesToGenerate; i++) {
            VenueDto savedVenue = saveVenue(venueDto);
            ArtistDto savedArtist = saveArtist(artistDto);
            anArtistId = savedArtist.getId();
            PerformanceDto perf = PerformanceDto.builder()
                .title(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .date(TestDataEvent.TEST_PERFORMANCE_DATE)
                .artist(savedArtist)
                .venue(savedVenue)
                .ticketTypes(TestDataTicket.getTicketTypeDtos())
                .build();
            this.mockMvc.perform(
                post(PERFORMANCE_BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(perf))
                    .header(securityProperties.getAuthHeader(), authToken)
            ).andReturn();
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI)
                .param("artistId", anArtistId.toString())
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(pageSize))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PerformanceDto[] performanceDtos = objectMapper.readValue(response.getContentAsString(), PerformanceDto[].class);
        assertEquals(1, performanceDtos.length);
    }

    @Test
    @DisplayName("Should return correctly sized chunk of performances on get by location")
    public void givenPerformance_whenGetByLocation_thenGetCorrectChunk() throws Exception {
        int amountOfPerformancesToGenerate = 15;
        int pageSize = 5;

        Long anAddressId = null;
        for (int i = 0; i < amountOfPerformancesToGenerate; i++) {
            VenueDto savedVenue = saveVenue(venueDto);
            anAddressId = savedVenue.getAddress().getId();
            ArtistDto savedArtist = saveArtist(artistDto);
            PerformanceDto perf = PerformanceDto.builder()
                .title(TestDataEvent.TEST_EVENT_TITLE)
                .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .date(TestDataEvent.TEST_PERFORMANCE_DATE)
                .artist(savedArtist)
                .venue(savedVenue)
                .ticketTypes(TestDataTicket.getTicketTypeDtos())
                .build();
            this.mockMvc.perform(
                post(PERFORMANCE_BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(perf))
                    .header(securityProperties.getAuthHeader(), authToken)
            ).andReturn();
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI)
                .param("addressId", anAddressId.toString())
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(pageSize))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PerformanceDto[] performanceDtos = objectMapper.readValue(response.getContentAsString(), PerformanceDto[].class);
        assertEquals(1, performanceDtos.length);
    }

    @Test
    @DisplayName("Should return 400 when Date is in past")
    public void whenCreatePerformanceWithPastDate_then400() throws Exception {
        PerformanceDto invalidPerformance = PerformanceDto.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE.minusYears(10))
            .artist(artistDto)
            .venue(venueDto)
            .ticketTypes(TestDataTicket.getTicketTypeDtos())
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

    // Methods for saving dependent entities

    private ArtistDto saveArtist(ArtistDto artistDto) throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(ARTIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(artistDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ArtistDto.class);
    }

    private VenueDto saveVenue(VenueDto venueDto) throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), VenueDto.class);
    }

    @Test
    @DisplayName("Should return 200 and performance with date from search")
    public void whenEventsGiven_SearchWithDate_ShouldReturn200AndPerformance() throws Exception {
        // Create
        MvcResult mvcResult = this.mockMvc.perform(
            post(PERFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(performanceDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        PerformanceDto savedDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

        // Get
        MvcResult mvcResultGet = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("date", savedDto.getDate().toString())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse responseGet = mvcResultGet.getResponse();
        assertEquals(HttpStatus.OK.value(), responseGet.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, responseGet.getContentType());

        PerformanceDto performanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

        assertEquals(performanceDto.getId(), savedDto.getId());
    }

    @Test
    @DisplayName("Should return 200 and performance with venue from search")
    public void whenEventsGiven_SearchWithVenue_ShouldReturn200AndPerformance() throws Exception {
        // Create
        MvcResult mvcResult = this.mockMvc.perform(
            post(PERFORMANCE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(performanceDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        PerformanceDto savedDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

        // Get
        MvcResult mvcResultGet = this.mockMvc.perform(
            get(PERFORMANCE_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("venue", savedDto.getVenue().getId().toString())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse responseGet = mvcResultGet.getResponse();
        assertEquals(HttpStatus.OK.value(), responseGet.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, responseGet.getContentType());

        PerformanceDto performanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

        assertEquals(performanceDto.getId(), savedDto.getId());
    }

}
