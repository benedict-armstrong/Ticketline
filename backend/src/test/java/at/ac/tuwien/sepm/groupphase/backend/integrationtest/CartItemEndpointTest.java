package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.*;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CartItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LayoutUnitMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CartItemEndpointTest implements TestDataCartItem, TestDataUser, TestDataVenue, TestDataArtist, TestDataEvent, TestAuthentification {
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

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private VenueMapper venueMapper;

    @Autowired
    private LayoutUnitMapper layoutUnitMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private LayoutUnitRepository layoutUnitRepository;

    private CartItemDto cartItem;

    @BeforeEach
    public void beforeEach() throws Exception {
        Address address = Address.builder()
            .name("Max Mustermann")
            .lineOne("Teststraße 2")
            .city("Wieadn")
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
            .name("VANUE1")
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

        cartItem = CartItemDto.builder()
            .tickets(ticketMapper.ticketSetToTicketDtoArray(ticketSet))
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

        MvcResult mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI + "/1")
                .content(
                    objectMapper.writeValueAsString(cartItem)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        CartItemDto cartItemDto = objectMapper.readValue(response.getContentAsString(), CartItemDto.class);
        assertAll(
            () -> assertNotNull(cartItemDto.getId()),
            () -> assertEquals(cartItem.getTickets().length, cartItemDto.getTickets().length)
        );
    }

    @Test
    @DisplayName("Should return the list of cartItems after creating one")
    public void whenCreateCartItem_thenGetAllCartItems_GetCreatedCartItem() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI  + "/1")
                .content(
                    objectMapper.writeValueAsString(cartItem)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        mvcResult = this.mockMvc.perform(
            get(CART_ITEM_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<CartItemDto> cartItemDtoList = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), CartItemDto[].class)
        );
        assertAll(
            () -> assertNotNull(cartItemDtoList),
            () -> assertEquals(1, cartItemDtoList.size())
        );
    }

    @Test
    @DisplayName("Should return empty list after deleting cartItem")
    public void whenGettingAllCartItems_afterInsertingCartItemAndDeletingCartItem_thenShouldGetEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI  + "/1")
                .content(
                    objectMapper.writeValueAsString(cartItem)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        CartItemDto newCartItemDto = objectMapper.readValue(response.getContentAsString(), CartItemDto.class);

        assertNotNull(newCartItemDto);

        mvcResult = this.mockMvc.perform(
            delete(CART_ITEM_BASE_URI + "/" + newCartItemDto.getId())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        mvcResult = this.mockMvc.perform(
            get(CART_ITEM_BASE_URI)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<CartItemDto> cartItemDtoList = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), CartItemDto[].class)
        );

        assertAll(
            () -> assertNotNull(cartItemDtoList),
            () -> assertEquals(0, cartItemDtoList.size())
        );
    }

    @Test
    @DisplayName("Should return new cartItem when adding a ticket to it")
    public void whenCreateCartItem_thenAddTicket_shouldGiveCorrectCartItem() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI  + "/1")
                .content(
                    objectMapper.writeValueAsString(cartItem)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        CartItemDto newCartItemDto = objectMapper.readValue(response.getContentAsString(), CartItemDto.class);

        mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI + "/" + newCartItemDto.getId() + "/addTicket")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        CartItemDto newNewCartItemDto = objectMapper.readValue(response.getContentAsString(), CartItemDto.class);
        assertAll(
            () -> assertEquals(newCartItemDto.getId() ,newNewCartItemDto.getId()),
            () -> assertEquals(2 ,newNewCartItemDto.getTickets().length)
        );
    }

    @Test
    @DisplayName("Should return NoTicketLeftException when adding too many ticket to it")
    public void whenCreateCartItem_thenAddManyTickets_shouldRespondWithNoTicketFoundException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI + "/1")
                .content(
                    objectMapper.writeValueAsString(cartItem)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        CartItemDto newCartItemDto = objectMapper.readValue(response.getContentAsString(), CartItemDto.class);

        mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI + "/" + newCartItemDto.getId() + "/addTicket")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI + "/" + newCartItemDto.getId() + "/addTicket")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI + "/" + newCartItemDto.getId() + "/addTicket")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI + "/" + newCartItemDto.getId() + "/addTicket")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 200 (true) when checkout")
    public void whenCreateCartItem_thenCheckout_thenCartItemShouldBePaidFor() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(CART_ITEM_BASE_URI  + "/1")
                .content(
                    objectMapper.writeValueAsString(cartItem)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        CartItemDto cartItemDto = objectMapper.readValue(response.getContentAsString(), CartItemDto.class);

        mvcResult = this.mockMvc.perform(
            put(CART_ITEM_BASE_URI + "/checkout")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        boolean succeeded = objectMapper.readValue(response.getContentAsString(), boolean.class);
        assertTrue(succeeded);

        mvcResult = this.mockMvc.perform(
            get(CART_ITEM_BASE_URI + "/paid")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<CartItemDto> cartItemDtoList = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), CartItemDto[].class)
        );

        List<CartItem> cartItemList = cartItemMapper.cartItemDtoListToCartItemList(cartItemDtoList);

        assertAll(
            () -> assertNotNull(cartItemList),
            () -> assertNotNull(cartItemList.get(0)),
            () -> assertEquals(CartItem.Status.PAID_FOR, cartItemList.get(0).getStatus())
        );
    }

    @Test
    @DisplayName("Should return 200 (false) when checkout with empty cart")
    public void whenCheckout_andEmptyCart_thenReturnFalse() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            put(CART_ITEM_BASE_URI + "/checkout")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        boolean succeeded = objectMapper.readValue(response.getContentAsString(), boolean.class);
        assertFalse(succeeded);
    }
}
