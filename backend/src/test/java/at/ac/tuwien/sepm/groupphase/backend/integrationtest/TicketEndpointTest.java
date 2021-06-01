package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataAddress;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataVenue;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LayoutUnitDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketTypeRepository;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TicketEndpointTest implements TestAuthentification, TestDataTicket {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    private String authToken;

    // Custom beans

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private SectorRepository sectorRepository;

    // Mappers

    @Autowired
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private PerformanceMapper performanceMapper;

    private final TicketDto template = TicketDto.builder()
        .seat(LayoutUnitDto.builder().build())
        .build();
    private final Artist artist = TestDataArtist.getArtist();

    private final TicketType ticketType = STANDARD_TICKET_TYPE;
    private final Performance performance = TestDataEvent.getPerformance(artist, Venue.builder().build());
    private final ApplicationUser user = TestDataUser.getUser();

    @BeforeEach
    public void beforeEach() throws Exception {
        artistRepository.save(artist);

        Sector sector = sectorRepository.save(TestDataVenue.getSeatedSector());
        Venue venue = TestDataVenue.getVenue();
        for (LayoutUnit layoutUnit : venue.getLayout()) {
            if (layoutUnit != null) {
                layoutUnit.setSector(sector);
            }
        }
        venue.setSectors(Collections.singletonList(sector));
        Venue savedVenue = venueRepository.save(venue);
        performance.setVenue(savedVenue);

        saveUser(user, userRepository, passwordEncoder);
        authToken = authenticate(user, mockMvc, objectMapper);

        TicketType savedTicketType = ticketTypeRepository.save(ticketType);

        Performance savedPerformance = performanceRepository.save(performance);

        template.setPerformance(performanceMapper.performanceToPerformanceDto(savedPerformance));
        template.setTicketType(ticketTypeMapper.ticketTypeToTicketTypeDto(savedTicketType));
    }

    @AfterEach
    public void afterEach() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        performanceRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        artistRepository.deleteAll();
        venueRepository.deleteAll();
        sectorRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save correct ticket when creating one")
    public void whenCreateTicket_thenGetBackCorrectTicket() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
                .param("mode", "buy")
                .content(
                    objectMapper.writeValueAsString(template)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        TicketDto ticketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);
        assertAll(
            () -> assertNotNull(ticketDto.getId()),
            () -> assertEquals(Ticket.Status.PAID.toString(), ticketDto.getStatus()),
            () -> assertEquals(template.getTotalPrice(), ticketDto.getTotalPrice())
        );
    }

}
