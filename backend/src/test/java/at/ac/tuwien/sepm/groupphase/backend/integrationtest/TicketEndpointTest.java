package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectorTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TicketEndpointTest implements TestAuthentification, TestDataTicket, TestDataEvent, TestDataUser {

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

    private TicketDto ticketDto;

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
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private PerformanceMapper performanceMapper;

    @BeforeEach
    public void beforeEach() throws Exception {
        Address address = Address.builder()
            .name("Max Mustermann")
            .lineOne("Teststraße 2")
            .city("Wien")
            .postcode("1010")
            .country("Österreich")
            .eventLocation(true)
            .build();
        Artist artist = Artist.builder().firstName("Test").lastName("Test2").build();

        address = addressRepository.save(address);
        artist = artistRepository.save(artist);

        SectorType sectorType = SectorType.builder().name("Test").numberOfTickets(10).build();
        TicketType ticketType = TicketType.builder().title("Test").price(10).sectorType(sectorType).build();

        Set<SectorType> sectorTypeSet = new HashSet<>();
        sectorTypeSet.add(sectorType);

        Set<TicketType> ticketTypeSet = new HashSet<>();
        ticketTypeSet.add(ticketType);

        Performance performance = Performance.builder()
            .title(TEST_EVENT_PERFORMANCE_TITLE)
            .description(TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(LocalDateTime.now())
            .artist(artist)
            .location(address)
            .ticketTypes(ticketTypeSet)
            .sectorTypes(sectorTypeSet)
            .build();

        performanceRepository.save(performance);

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

        saveUser(user, userRepository, passwordEncoder);
        authToken = authenticate(user, mockMvc, objectMapper);

        ticketDto = TicketDto.builder()
            .ticketType(ticketTypeMapper.ticketTypeToTicketTypeDto(ticketType))
            .seats(TICKET_SEATS)
            .ownerId(user.getId())
            .performance(performanceMapper.performanceToPerformanceDto(performance))
            .build();
    }

    @AfterEach
    public void afterEach() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        performanceRepository.deleteAll();
        artistRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save correct ticket when creating one")
    public void whenCreateTicket_thenGetBackCorrectTicket() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/cart")
                .content(
                    objectMapper.writeValueAsString(ticketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        TicketDto newTicketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);
        assertAll(
            () -> assertNotNull(newTicketDto.getId()),
            () -> assertEquals(Ticket.Status.IN_CART.toString(), newTicketDto.getStatus()),
            () -> assertEquals(ticketDto.getOwnerId(), newTicketDto.getOwnerId()),
            () -> assertEquals(ticketDto.getSeats(), newTicketDto.getSeats()),
            () -> assertEquals(ticketDto.getTicketType(), newTicketDto.getTicketType())
        );
    }

    @Test
    @DisplayName("Should return 404 when creating ticket with to many seats")
    public void whenCreateTicketWithToManySeats_ThrowNoTicketsLeftException() throws Exception {
        ticketDto.setSeats(Arrays.asList(1000L));

        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/cart")
                .content(
                    objectMapper.writeValueAsString(ticketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 200 when updating tickets with enough seats")
    public void whenCreateTicket_thenUpdateTicket_thenGetCorrectUpdateResponse() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/cart")
                .content(
                    objectMapper.writeValueAsString(ticketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        TicketDto newTicketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);

        TicketUpdateDto ticketUpdateDto = TicketUpdateDto.builder().id(newTicketDto.getId()).seats(Arrays.asList(1L)).build();

        mvcResult = this.mockMvc.perform(
            put(TICKET_BASE_URI + "/amount")
                .content(
                    objectMapper.writeValueAsString(ticketUpdateDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        TicketUpdateDto newTicketUpdateDto = objectMapper.readValue(response.getContentAsString(), TicketUpdateDto.class);
        assertAll(
            () -> assertEquals(ticketUpdateDto.getId() ,newTicketUpdateDto.getId()),
            () -> assertEquals(ticketUpdateDto.getSeats(), newTicketUpdateDto.getSeats()),
            () -> assertNotEquals(ticketDto.getSeats(), newTicketUpdateDto.getSeats())
        );
    }

    @Test
    @DisplayName("Should return 400 when updating tickets with not enough seats")
    public void whenCreateTicket_thenUpdateTicketWithToManySeats_thenThrowNoTicketsFoundException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/cart")
                .content(
                    objectMapper.writeValueAsString(ticketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        TicketDto newTicketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);

        TicketUpdateDto ticketUpdateDto = TicketUpdateDto.builder().id(newTicketDto.getId()).seats(Arrays.asList(100L)).build();

        mvcResult = this.mockMvc.perform(
            put(TICKET_BASE_URI + "/amount")
                .content(
                    objectMapper.writeValueAsString(ticketUpdateDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return list with all tickets")
    public void whenGettingAllCartTickets_afterInsertingTicket_thenShouldGetListOfTickets() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/cart")
                .content(
                    objectMapper.writeValueAsString(ticketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        TicketDto newTicketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);

        mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI + "/cart")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<TicketDto> ticketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );

        assertAll(
            () -> assertEquals(ticketDtos.size(), 1),
            () -> assertEquals(ticketDtos.get(0).getId(), newTicketDto.getId())
        );
    }

    @Test
    @DisplayName("Should return empty list after deleting ticket")
    public void whenGettingAllCartTickets_afterInsertingTicket_thenDeletingTicket_thenShouldGetEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/cart")
                .content(
                    objectMapper.writeValueAsString(ticketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        TicketDto newTicketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);

        mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI + "/cart")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        mvcResult = this.mockMvc.perform(
            delete(TICKET_BASE_URI + "/" + newTicketDto.getId())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        mvcResult = this.mockMvc.perform(
            get(TICKET_BASE_URI + "/cart")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<TicketDto> ticketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );

        assertAll(
            () -> assertEquals(ticketDtos.size(), 0)
        );
    }

    @Test
    @DisplayName("Should return 200 when updating tickets and canceling it")
    public void whenCreateTicket_thenCancelTicket_thenGetCorrectResponse() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/cart")
                .content(
                    objectMapper.writeValueAsString(ticketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        TicketDto newTicketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);

        mvcResult = this.mockMvc.perform(
            put(TICKET_BASE_URI + "/" + newTicketDto.getId() + "/cancel")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        TicketDto newNewTicketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);
        assertAll(
            () -> assertEquals(newTicketDto.getId() ,newNewTicketDto.getId()),
            () -> assertEquals(newTicketDto.getSeats(), newNewTicketDto.getSeats()),
            () -> assertEquals(Ticket.Status.CANCELLED.toString(), newNewTicketDto.getStatus())
        );
    }

    @Test
    @DisplayName("Should return 200 (true) when checkout")
    public void whenCreateTicket_thenCheckout_thenTicketShouldBePaidFor() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TICKET_BASE_URI + "/cart")
                .content(
                    objectMapper.writeValueAsString(ticketDto)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        TicketDto newTicketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);

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

        List<TicketDto> ticketDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TicketDto[].class)
        );

        List<Ticket> tickets = ticketMapper.ticketDtoListToTicketList(ticketDtos);

        assertAll(
            () -> assertEquals(Ticket.Status.PAID_FOR, tickets.get(0).getStatus())
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
