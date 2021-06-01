package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CartItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BookingEndpointTest implements TestAuthentification, TestDataTicket, TestDataEvent, TestDataUser {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    private String authToken;

    private BookingDto bookingDto;

    private Booking booking;

    // Custom beans

    @Autowired
    private CartItemRepository cartItemRepository;

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

    // Mappers

    @Autowired
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private PerformanceMapper performanceMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @BeforeEach
    public void beforeEach() throws Exception {
        Address address = Address.builder()
            .name("Max Mustermann")
            .lineOne("Teststraße 2")
            .city("Linz")
            .postcode("1010")
            .country("Österreich")
            .eventLocation(true)
            .build();

        ApplicationUser user = ApplicationUser.builder()
            .firstName(ADMIN_FIRST_NAME)
            .lastName(ADMIN_LAST_NAME)
            .email(ADMIN_EMAIL)
            .lastLogin(ADMIN_LAST_LOGIN)
            .role(ADMIN_ROLE)
            .status(ADMIN_USER_STATUS)
            .password(ADMIN_PASSWORD)
            .points(ADMIN_POINTS)
            .address(Address.builder()
                .name("Max Mustermann")
                .lineOne("Teststraße 2")
                .city("Wien")
                .postcode("1010")
                .country("Österreich")
                .eventLocation(false)
                .build())
            .telephoneNumber(ADMIN_PHONE_NUMBER)
            .build();

        ApplicationUser savedUser = saveUser(user, userRepository, passwordEncoder);
        authToken = authenticate(user, mockMvc, objectMapper);

        Artist savedArtist = artistRepository.save(Artist.builder()
            .firstName("First")
            .lastName("Last")
            .build()
        );

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

        TicketType ticketType = TicketType.builder().title("TicketType1").price(1000L).sector(sector).build();
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
            .name("VENUE1")
            .address(address)
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

        Ticket ticket1 = Ticket.builder()
            .ticketType(ticketType)
            .performance(savedPerformance)
            .seat(layoutUnitList.get(0))
            .build();
        Set<Ticket> ticketSet = new HashSet<>();
        ticketSet.add(ticket1);

        CartItem cartItem = cartItemRepository.save(CartItem.builder()
            .status(CartItem.Status.PAID_FOR)
            .changeDate(LocalDateTime.now())
            .tickets(ticketSet)
            .user(savedUser)
            .build());
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);

        CartItemDto[] dtoArray = cartItemMapper.cartItemSetToCartItemDtoArray(cartItems);

        bookingDto = BookingDto.builder()
            .cartItems(dtoArray)
            .build();


        booking = Booking.builder()
            .buyDate(LocalDateTime.now())
            .user(savedUser)
            .cartItems(cartItems)
            .invoice(null)
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
    @DisplayName("Should save correct booking when creating one")
    public void whenCreateBooking_thenGetBackCorrectBooking() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(BASE_URI + "/bookings")
                .content(
                    objectMapper.writeValueAsString(bookingDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        BookingDto newBookingDto = objectMapper.readValue(response.getContentAsString(), BookingDto.class);
        assertAll(
            () -> assertNotNull(newBookingDto.getId()),
            () -> assertEquals(bookingDto.getInvoice(), newBookingDto.getInvoice())
        );
    }

    @Test
    @DisplayName("Should give back list of bookings of the user")
    public void whenCreateBooking_thenGetBackCorrectBooking1() throws Exception {
        bookingRepository.save(booking);

        MvcResult mvcResult = this.mockMvc.perform(
            get(BASE_URI + "/bookings")
                .content(
                    objectMapper.writeValueAsString(bookingDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        BookingDto[] newBookingDtoArray = objectMapper.readValue(response.getContentAsString(), BookingDto[].class);
        assertAll(
            () -> assertNotEquals(newBookingDtoArray.length, 0)
        );
    }
}