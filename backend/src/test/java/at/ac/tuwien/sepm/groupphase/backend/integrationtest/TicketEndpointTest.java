package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.*;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TicketEndpointTest implements TestDataTicket, TestDataUser, TestDataVenue, TestDataArtist, TestDataEvent, TestAuthentification {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    private String authToken;

    @Autowired
    private TicketRepository cartItemRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PerformanceMapper performanceMapper;

    @Autowired
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private LayoutUnitRepository layoutUnitRepository;

    private Ticket ticket;

    private NewTicketDto newTicketDto;

    private final int ticketAmount = 1;

    @BeforeEach
    public void beforeEach() throws Exception {
        ApplicationUser user = TestDataUser.getUser();
        ApplicationUser savedUser = saveUser(user, userRepository, passwordEncoder);
        authToken = authenticate(user, mockMvc, objectMapper);

        Artist savedArtist = artistRepository.save(TestDataArtist.getArtist());

        Sector sector = Sector.builder()
            .name("Sector1")
            .color("BLUE")
            .type(Sector.SectorType.STANDING)
            .description("THIS IS A SECTOR TYPE")
            .localId(1L)
            .build();

        Set<Sector> sectorSet = new HashSet<>();
        sectorSet.add(sector);
        List<Sector> sectorList = sectorRepository.saveAll(new LinkedList<>(sectorSet));

        TicketType ticketType = TicketType.builder().title("TicketType1").price(1000L).sector(sectorList.get(0)).build();
        Set<TicketType> ticketTypeSet = new HashSet<>();
        ticketTypeSet.add(ticketType);

        List<LayoutUnit> layoutUnitList = new LinkedList<>();
        layoutUnitList.add(LayoutUnit.builder()
            .customLabel("0")
            .sector(sector)
            .localId(0)
            .build());
        layoutUnitList.add(LayoutUnit.builder()
            .customLabel("1")
            .sector(sector)
            .localId(1)
            .build());
        layoutUnitList.add(LayoutUnit.builder()
            .customLabel("2")
            .sector(sector)
            .localId(2)
            .build());
        layoutUnitList.add(LayoutUnit.builder()
            .customLabel("3")
            .sector(sector)
            .localId(3)
            .build());

        Venue savedVenue = venueRepository.save(Venue.builder()
            .sectors(sectorList)
            .layout(layoutUnitList)
            .name("VANUE1")
            .address(TestDataAddress.getAddress())
            .width(1)
            .owner(savedUser)
            .build()
        );

        Performance savedPerformance = performanceRepository.save(Performance.builder()
            .ticketTypes(ticketTypeSet)
            .date(LocalDateTime.now())
            .artist(savedArtist)
            .description("THIS IS A PERFORMANCE")
            .title("PERFORMANCE 1")
            .venue(savedVenue)
            .build()
        );

        newTicketDto = NewTicketDto.builder()
            .performance(performanceMapper.performanceToPerformanceDto(savedPerformance))
            .ticketType(ticketTypeMapper.ticketTypeToTicketTypeDto(ticketType))
            .build();
    }

    @AfterEach
    public void afterEach () {
        bookingRepository.deleteAll();
        cartItemRepository.deleteAll();
        performanceRepository.deleteAll();
        venueRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save correct cartItem when creating one")
    public void whenCreateCartItem_thenGetBackCorrectCartItem() throws Exception {

        System.out.println(objectMapper.writeValueAsString(newTicketDto));

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/" + ticketAmount)
                .content(
                    objectMapper.writeValueAsString(newTicketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<TicketDto> ticketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );

        assertAll(
            () -> assertNotNull(ticketDtos.get(0).getId()),
            () -> assertEquals(ticketAmount, ticketDtos.size())
        );
    }

    @Test
    @DisplayName("Should return the list of cartItems after creating one")
    public void whenCreateCartItem_thenGetAllCartItems_GetCreatedCartItem() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI  + "/" + ticketAmount)
                .content(
                    objectMapper.writeValueAsString(newTicketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<TicketDto> ticketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );
        assertAll(
            () -> assertNotNull(ticketDtos),
            () -> assertEquals(1, ticketDtos.size())
        );
    }

    @Test
    @DisplayName("Should return empty list after deleting cartItem")
    public void whenGettingAllCartItems_afterInsertingCartItemAndDeletingCartItem_thenShouldGetEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI  + "/" + ticketAmount)
                .content(
                    objectMapper.writeValueAsString(newTicketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        List<TicketDto> ticketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );

        assertNotNull(ticketDtos.get(0));

        mvcResult = this.mockMvc.perform(
            delete(TICKET_BASE_URI + "/" + ticketDtos.get(0).getId())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<TicketDto> newTicketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );

        assertAll(
            () -> assertNotNull(newTicketDtos),
            () -> assertEquals(0, newTicketDtos.size())
        );
    }


    @Test
    @DisplayName("Should return 200 (true) when checkout")
    public void whenCreateCartItem_thenCheckout_thenCartItemShouldBePaidFor() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI  + "/" + ticketAmount)
                .content(
                    objectMapper.writeValueAsString(newTicketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        List<TicketDto> ticketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );

        mvcResult = this.mockMvc.perform(
            put(TICKET_BASE_URI + "/checkout")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        boolean succeeded = objectMapper.readValue(response.getContentAsString(), boolean.class);
        assertTrue(succeeded);

        mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI + "/paid")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<TicketDto> newTicketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );

        List<Ticket> ticketList = ticketMapper.ticketDtoListToTicketList(newTicketDtos);

        assertAll(
            () -> assertNotNull(ticketList),
            () -> assertNotNull(ticketList.get(0)),
            () -> assertEquals(Ticket.Status.PAID_FOR, ticketList.get(0).getStatus())
        );
    }

    @Test
    @DisplayName("Should return 200 (false) when checkout with empty cart")
    public void whenCheckout_andEmptyCart_thenReturnFalse() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            put(TICKET_BASE_URI + "/checkout")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        boolean succeeded = objectMapper.readValue(response.getContentAsString(), boolean.class);
        assertFalse(succeeded);
    }
}
