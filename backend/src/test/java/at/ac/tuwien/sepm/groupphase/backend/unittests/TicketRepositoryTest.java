package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataVenue;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TicketRepositoryTest implements TestDataUser, TestDataVenue, TestDataTicket {
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

    private Ticket ticket;

    @BeforeEach
    public void beforeEach() throws Exception {
        ApplicationUser savedUser = userRepository.save(TestDataUser.getAdmin());
        Artist savedArtist = artistRepository.save(TestDataArtist.getArtist());

        Venue savedVenue = venueRepository.save(TestDataVenue.getVenue());
        sectorRepository.saveAll(savedVenue.getSectors());

        Performance savedPerformance = performanceRepository.save(Performance.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE)
            .artist(savedArtist)
            .ticketTypes(TestDataTicket.getTicketTypes())
            .venue(savedVenue)
            .build()
        );

        ticket = Ticket.builder()
            .user(savedUser)
            .ticketType(savedPerformance.getTicketTypes().iterator().next())
            .performance(savedPerformance)
            .seat(savedVenue.getLayout().get(0))
            .changeDate(LocalDateTime.now())
            .status(Ticket.Status.IN_CART)
            .build();
    }

    @AfterEach
    public void afterEach () {
        ticketRepository.deleteAll();
        performanceRepository.deleteAll();
        venueRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        sectorRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return correct entity back after create")
    public void whenCreateNew_thenGetCorrectEntityBack() {
        Ticket savedTicket = ticketRepository.save(ticket);

        assertAll(
            () -> assertNotNull(savedTicket.getId()),
            () -> assertEquals(ticket.getUser(), savedTicket.getUser()),
            () -> assertEquals(ticket.getStatus(), savedTicket.getStatus()),
            () -> assertEquals(ticket.getPerformance(), savedTicket.getPerformance()),
            () -> assertEquals(ticket.getTicketType(), savedTicket.getTicketType()),
            () -> assertEquals(ticket.getSeat(), savedTicket.getSeat()),
            () -> assertEquals(ticket.getChangeDate(), savedTicket.getChangeDate())
        );
    }
}
