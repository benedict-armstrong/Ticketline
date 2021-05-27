package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketTypeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TicketRepositoryTest implements TestDataEvent, TestDataTicket, TestDataUser {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private EventRepository performanceRepository;

    @Autowired
    private UserRepository userRepository;

    private final Ticket template = STANDARD_TICKET;
    //    private final Performance performance = Performance.builder()
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
        ticketTypeRepository.deleteAll();
        performanceRepository.deleteAll();
        userRepository.deleteAll();

        ApplicationUser savedUser = userRepository.save(user);
        user.setId(savedUser.getId());

        ticketTypeRepository.save(STANDARD_TICKET_TYPE);

        //        Event savedPerformance = performanceRepository.save(performance);
        //        SectorType sectorType = (SectorType) savedPerformance.getSectorTypes().toArray()[0];
        //        sectorType.setPrice(100L);

        //        template.setSectorType(sectorType);
        template.setOwner(savedUser);
        //        template.setPerformance(savedPerformance);
        template.setStatus(Ticket.Status.PAID_FOR);
    }

//    @Test
//    @DisplayName("Should return correct entity back after create")
//    public void whenCreateNew_thenGetCorrectEntityBack() {
//        Long templatePrice = (long) Math.floor(100 * template.getTicketType().getPriceMultiplier()
//            * template.getSectorType().getPrice() * template.getSeats().size());
//        template.setTotalPrice(templatePrice);
//
//        Ticket ticket = ticketRepository.save(template);
//        assertAll(
//            () -> assertNotNull(ticket.getId()),
//            () -> assertEquals(template.getOwner(), ticket.getOwner()),
//            () -> assertEquals(template.getPerformance(), ticket.getPerformance()),
//            () -> assertEquals(template.getSectorType(), ticket.getSectorType()),
//            () -> assertEquals(template.getSeats(), ticket.getSeats()),
//            () -> assertEquals(template.getTicketType(), ticket.getTicketType()),
//            () -> assertEquals(templatePrice, ticket.getTotalPrice()),
//            () -> assertEquals(template.getStatus(), ticket.getStatus())
//        );
//    }

}
