package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
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
    private final Address location = TestDataEvent.getLocation();
    private final Performance performance = TestDataEvent.getPerformance(artist, location);
    private final ApplicationUser user = TestDataUser.getAdmin();

    @BeforeEach
    public void beforeEach() throws Exception {
        user.setAddress(location);

        ApplicationUser savedUser = userRepository.save(user);
        user.setId(savedUser.getId());

        ticketTypeRepository.save(STANDARD_TICKET_TYPE);

        Artist savedArtist = artistRepository.save(artist);

        Performance savedPerformance = performanceRepository.save(performance);
        savedPerformance.setArtist(savedArtist);

        SectorType sectorType = (SectorType) savedPerformance.getSectorTypes().toArray()[0];
        sectorType.setPrice(100L);

        template.setSectorType(sectorType);
        template.setOwner(savedUser);
        template.setPerformance(savedPerformance);
        template.setStatus(Ticket.Status.PAID_FOR);
    }

    @AfterEach
    public void afterEach() {
        ticketRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        performanceRepository.deleteAll();
        userRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return correct entity back after create")
    public void whenCreateNew_thenGetCorrectEntityBack() {
        Long templatePrice = (long) Math.floor(100 * template.getTicketType().getMultiplier()
            * template.getSectorType().getPrice() * template.getSeats().size());
        template.setTotalPrice(templatePrice);

        Ticket ticket = ticketRepository.save(template);
        assertAll(
            () -> assertNotNull(ticket.getId()),
            () -> assertEquals(template.getOwner(), ticket.getOwner()),
            () -> assertEquals(template.getPerformance(), ticket.getPerformance()),
            () -> assertEquals(template.getSectorType(), ticket.getSectorType()),
            () -> assertEquals(template.getSeats(), ticket.getSeats()),
            () -> assertEquals(template.getTicketType(), ticket.getTicketType()),
            () -> assertEquals(templatePrice, ticket.getTotalPrice()),
            () -> assertEquals(template.getStatus(), ticket.getStatus())
        );
    }

}
