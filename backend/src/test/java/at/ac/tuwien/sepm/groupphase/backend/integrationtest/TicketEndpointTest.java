package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataAddress;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataVenue;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatCountDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LayoutUnitRepository;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private TicketRepository ticketRepository;

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

    private NewTicketDto newTicketDtoLarge;

    private NewTicketDto newTicketDtoLarger;

    private NewTicketDto newTicketSeatDto;

    private final int ticketAmount = 1;

    private final int ticketAmountLarge = 10;

    private final int ticketAmountLarger = 10000;

    private Performance savedPerformance;

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

        savedPerformance = performanceRepository.save(Performance.builder()
            .ticketTypes(ticketTypeSet)
            .date(LocalDateTime.now())
            .artist(savedArtist)
            .description("THIS IS A PERFORMANCE")
            .title("PERFORMANCE 1")
            .venue(savedVenue)
            .build()
        );

        newTicketSeatDto = NewTicketDto.builder()
            .performanceId(performanceMapper.performanceToPerformanceDto(savedPerformance).getId())
            .ticketType(ticketTypeMapper.ticketTypeToTicketTypeDto(ticketType))
            .amount(0)
            .seatId(savedPerformance.getVenue().getLayout().get(0).getId())
            .build();

        newTicketDto = NewTicketDto.builder()
            .performanceId(performanceMapper.performanceToPerformanceDto(savedPerformance).getId())
            .ticketType(ticketTypeMapper.ticketTypeToTicketTypeDto(ticketType))
            .amount(ticketAmount)
            .seatId(null)
            .build();

        newTicketDtoLarge = NewTicketDto.builder()
            .performanceId(performanceMapper.performanceToPerformanceDto(savedPerformance).getId())
            .ticketType(ticketTypeMapper.ticketTypeToTicketTypeDto(ticketType))
            .amount(ticketAmountLarge)
            .seatId(null)
            .build();

        newTicketDtoLarger = NewTicketDto.builder()
            .performanceId(performanceMapper.performanceToPerformanceDto(savedPerformance).getId())
            .ticketType(ticketTypeMapper.ticketTypeToTicketTypeDto(ticketType))
            .amount(ticketAmountLarger)
            .seatId(null)
            .build();
    }

    @AfterEach
    public void afterEach () {
        bookingRepository.deleteAll();
        ticketRepository.deleteAll();
        performanceRepository.deleteAll();
        venueRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save correct ticket when creating one")
    public void whenCreateTicket_thenGetBackCorrectTicket() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
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
    @DisplayName("Should save correct ticket when creating one")
    public void whenCreateTicket_WithSeatId_thenGetBackCorrectTicket() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
                .content(
                    objectMapper.writeValueAsString(newTicketSeatDto)
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
            () -> assertEquals(1, ticketDtos.size())
        );
    }

    @Test
    @DisplayName("Should return 400 from FullCartException")
    public void whenCreateTicket_butMoreThanMaxCartSize_thenFullCartException() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
                .content(
                    objectMapper.writeValueAsString(newTicketDtoLarger)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 404 from NoTicketLeftException")
    public void whenCreateTicket_butMoreThenFreeSeats_thenNoTicketLeftException() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
                .content(
                    objectMapper.writeValueAsString(newTicketDtoLarge)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return the list of tickets after creating one")
    public void whenCreateTicket_thenGetAllTickets_GetCreatedTicket() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
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
    @DisplayName("Should return empty list after deleting ticket")
    public void whenGettingAllTickets_afterInsertingTicketAndDeletingTicket_thenShouldGetEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
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
    @DisplayName("Should return empty list after deleting ticket")
    public void whenGettingAllTickets_afterInsertingTicketAndDeletingTicketUsingMassDelete_thenShouldGetEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
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

        List<Long> idList = new LinkedList<Long>();
        for (TicketDto ticketDto : ticketDtos) {
            idList.add(ticketDto.getId());
        }

        mvcResult = this.mockMvc.perform(
            delete(TICKET_BASE_URI)
                .content(objectMapper.writeValueAsString(idList))
                .contentType(MediaType.APPLICATION_JSON)
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
    public void whenCreateTicket_thenCheckout_thenTicketShouldBePaidFor() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
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

    @Test
    @DisplayName("Should return 200 (true) when checkout")
    public void whenCreateTicket_thenReserve_thenTicketShouldReserved() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
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
            put(TICKET_BASE_URI + "/reserve")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        boolean succeeded = objectMapper.readValue(response.getContentAsString(), boolean.class);
        assertTrue(succeeded);

        mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI + "/reserved")
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
            () -> assertEquals(Ticket.Status.RESERVED, ticketList.get(0).getStatus())
        );
    }

    @Test
    @DisplayName("Should return 200 (false) when reserve with empty cart")
    public void whenReserve_andEmptyCart_thenReturnFalse() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            put(TICKET_BASE_URI + "/reserve")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        boolean succeeded = objectMapper.readValue(response.getContentAsString(), boolean.class);
        assertFalse(succeeded);
    }

    @Test
    @DisplayName("Should return list of seatCounts")
    public void whenCreateTicket_thenRequestSeatCounts_shouldReturnSeatCounts() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI)
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

        Long performanceId = ticketDtos.get(0).getPerformance().getId();

        mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI + "/" + performanceId + "/seatCounts")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SeatCountDto> seatCountDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), SeatCountDto[].class)
        );

        assertAll(
            () -> assertNotNull(seatCountDtos),
            () -> assertNotNull(seatCountDtos.get(0)),
            () -> assertEquals(savedPerformance.getVenue().getSectors().get(0).getId(), seatCountDtos.get(0).getSectorId()),
            () -> assertEquals(4, seatCountDtos.get(0).getTotalSeats()),
            () -> assertEquals(4 - ticketAmount, seatCountDtos.get(0).getFreeSeats())
        );
    }

    @Test
    @DisplayName("When there are no tickets all Elements in Sales should be 0.0d")
    public void whenNoTickets_thenSalesListOnlyZeros() throws Exception{
        MvcResult mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI + "/sales")
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        Double[] list = objectMapper.readValue(response.getContentAsString(), Double[].class);

        for (Double d : list) {
            assertEquals(d, 0.0d);
        }

    }

//    @Test
//    @DisplayName("When there is just one paid for ticket one of the Elements in Sales should be 1.0d and the rest 0.0d")
//    public void whenOneTicket_thenSalesListOneAndRestZeros() throws Exception{
//
//        this.mockMvc.perform(
//            post(TICKET_BASE_URI + "/" + ticketAmount)
//                .content(
//                    objectMapper.writeValueAsString(newTicketDto)
//                )
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(securityProperties.getAuthHeader(), authToken)
//        ).andReturn();
//
//        this.mockMvc.perform(
//            put(TICKET_BASE_URI + "/checkout")
//                .header(securityProperties.getAuthHeader(), authToken)
//        ).andReturn();
//
//        MvcResult mvcResult = this.mockMvc.perform(
//            get(TICKET_BASE_URI + "/sales")
//        ).andReturn();
//        MockHttpServletResponse response = mvcResult.getResponse();
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
//
//        List<Double> list = Arrays.asList(
//            objectMapper.readValue(response.getContentAsString(), Double[].class)
//        );
//
//        assert(list.contains(1.0d));
//
//    }

}
