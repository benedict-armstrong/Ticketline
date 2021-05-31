package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketTypeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
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
public class TicketRepositoryTest implements TestDataTicket {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private final Ticket template = STANDARD_TICKET;
    private final Artist artist = TestDataArtist.getArtist();
    private final Performance performance = TestDataEvent.getPerformance(artist, Venue.builder().build());
    private final ApplicationUser user = TestDataUser.getAdmin();

    @BeforeEach
    public void beforeEach() throws Exception {


        ApplicationUser savedUser = userRepository.save(user);
        user.setId(savedUser.getId());

        TicketType savedTicketType = ticketTypeRepository.save(STANDARD_TICKET_TYPE);

        Artist savedArtist = artistRepository.save(artist);

        Performance savedPerformance = performanceRepository.save(performance);
        savedPerformance.setArtist(savedArtist);

        template.setOwner(savedUser);
        template.setPerformance(savedPerformance);
        template.setStatus(Ticket.Status.PAID);
        template.setTicketType(savedTicketType);
    }

    @AfterEach
    public void afterEach() {
        ticketRepository.deleteAll();
        performanceRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        userRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return correct entity back after create")
    public void whenCreateNew_thenGetCorrectEntityBack() {


        Ticket ticket = ticketRepository.save(template);
        assertAll(
            () -> assertNotNull(ticket.getId()),
            () -> assertEquals(template.getOwner(), ticket.getOwner()),
            () -> assertEquals(template.getPerformance(), ticket.getPerformance()),
            () -> assertEquals(template.getSeat(), ticket.getSeat()),
            () -> assertEquals(template.getTicketType(), ticket.getTicketType()),
            () -> assertEquals(template.getTotalPrice(), ticket.getTotalPrice()),
            () -> assertEquals(template.getStatus(), ticket.getStatus())
        );
    }

}
