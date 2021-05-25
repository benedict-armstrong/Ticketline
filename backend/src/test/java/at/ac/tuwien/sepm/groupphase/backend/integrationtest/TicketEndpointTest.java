package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketTypeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TicketEndpointTest implements TestAuthentification, TestDataTicket, TestDataEvent {

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
    private EventRepository performanceRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketTypeMapper ticketTypeMapper;

    private final TicketDto template = TicketDto.builder()
        .seats(Arrays.asList(1L, 5L, 6L))
        .build();
    //    private final Event performance = Event.builder()
    //        .title(TEST_EVENT_TITLE)
    //        .description(TEST_EVENT_DESCRIPTION)
    //        .date(TEST_EVENT_DATE_FUTURE)
    //        .duration(TEST_EVENT_DURATION)
    //        .eventType(TEST_EVENT_EVENT_TYPE)
    //        .artist(TestDataEvent.getTestEventArtist())
    //        .location(TestDataEvent.getTestEventLocation())
    //        .sectorTypes(TestDataEvent.getTestEventSectortypes())
    //        .build();
    private final Address address = Address.builder()
        .name("Max Mustermann")
        .lineOne("Teststraße 2")
        .city("Wien")
        .postcode("1010")
        .country("Österreich")
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
        .address(address)
        .telephoneNumber(ADMIN_PHONE_NUMBER)
        .build();

    @BeforeEach
    public void beforeEach() throws Exception {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        performanceRepository.deleteAll();

        ApplicationUser savedUser = saveUser(user, userRepository, passwordEncoder);
        authToken = authenticate(user, mockMvc, objectMapper);

        ticketTypeRepository.save(STANDARD_TICKET_TYPE);

        //        Event savedPerformance = performanceRepository.save(performance);
        //        SectorType sectorType = (SectorType) savedPerformance.getSectorTypes().toArray()[0];
        //        sectorType.setPrice(100L);

        //        template.setSectorType(sectorType);
        //        template.setOwner(savedUser.getId());
        //        template.setPerformance(savedPerformance.getId());
        //        template.setTicketType(ticketTypeMapper.ticketTypeToTicketTypeDto(STANDARD_TICKET_TYPE));
    }

//    @Test
//    @DisplayName("Should save correct ticket when creating one")
//    public void whenCreateTicket_thenGetBackCorrectTicket() throws Exception {
//        Long templatePrice = (long) Math.floor(100 * template.getTicketType().getPriceMultiplier()
//            * template.getSectorType().getPrice() * template.getSeats().size());
//
//        MvcResult mvcResult = this.mockMvc.perform(
//            post(TICKET_BASE_URI)
//                .param("mode", "buy")
//                .content(
//                    objectMapper.writeValueAsString(template)
//                )
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(securityProperties.getAuthHeader(), authToken)
//        ).andReturn();
//        MockHttpServletResponse response = mvcResult.getResponse();
//        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
//        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
//
//        TicketDto ticketDto = objectMapper.readValue(response.getContentAsString(), TicketDto.class);
//        assertAll(
//            () -> assertNotNull(ticketDto.getId()),
//            () -> assertEquals(Ticket.Status.PAID_FOR.toString(), ticketDto.getStatus()),
//            () -> assertEquals(templatePrice, ticketDto.getTotalPrice())
//        );
//    }

}
