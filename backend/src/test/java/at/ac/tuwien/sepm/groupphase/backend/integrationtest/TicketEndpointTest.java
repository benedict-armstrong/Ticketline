package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataAddress;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectorTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketTypeRepository;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TicketEndpointTest implements TestAuthentification, TestDataTicket, TestDataEvent, TestDataArtist {

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

    // Mappers

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private SectorTypeMapper sectorTypeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PerformanceMapper performanceMapper;

    private final TicketDto template = TicketDto.builder()
        .seats(Arrays.asList(1L, 5L, 6L))
        .build();
    private final Artist artist = TestDataArtist.getArtist();
//    private final Address address = TestDataAddress.getAddress();
    private final Address address = Address.builder()
        .name("Max Mustermann")
        .lineOne("Teststraße 2")
        .city("Wien")
        .postcode("1010")
        .country("Österreich")
        .build();
    private final TicketType ticketType = STANDARD_TICKET_TYPE;
    private final Performance performance = Performance.builder()
        .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
        .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
        .date(TestDataEvent.TEST_PERFORMANCE_DATE)
        .artist(artist)
        .location(address)
        .sectorTypes(TestDataEvent.getTestEventSectortypes())
        .ticketTypes(TestDataTicket.getTicketTypes())
        .build();
    private final ApplicationUser user = ApplicationUser.builder()
        .firstName(ADMIN_FIRST_NAME)
        .lastName(ADMIN_LAST_NAME)
        .email(ADMIN_EMAIL)
        .lastLogin(ADMIN_LAST_LOGIN)
        .role(ADMIN_ROLE)
        .status(ADMIN_USER_STATUS)
        .password(ADMIN_PASSWORD)
        .points(ADMIN_POINTS)
        .address(TestDataAddress.getAddress())
        .telephoneNumber(ADMIN_PHONE_NUMBER)
        .build();

    /*@BeforeEach
    public void beforeEach() throws Exception {
        artistRepository.save(artist);
        addressRepository.save(address);

        saveUser(user, userRepository, passwordEncoder);
        authToken = authenticate(user, mockMvc, objectMapper);

        ticketTypeRepository.save(ticketType);

        Performance savedPerformance = performanceRepository.save(performance);
        SectorType sectorType = (SectorType) savedPerformance.getSectorTypes().toArray()[0];

        template.setPerformance(performanceMapper.performanceToPerformanceDto(savedPerformance));
        template.setTicketType(ticketTypeMapper.ticketTypeToTicketTypeDto(STANDARD_TICKET_TYPE));
    }

    @AfterEach
    public void afterEach() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        performanceRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        artistRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save correct ticket when creating one")
    public void whenCreateTicket_thenGetBackCorrectTicket() throws Exception {
        Long templatePrice = (long) Math.floor(100 * template.getTicketType().getMultiplier()
            * template.getSectorType().getPrice() * template.getSeats().size());

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
            () -> assertEquals(Ticket.Status.PAID_FOR.toString(), ticketDto.getStatus()),
            () -> assertEquals(templatePrice, ticketDto.getTotalPrice())
        );
    }*/

}
