package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
/*
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BookingRepositoryTest implements TestDataTicket, TestDataEvent, TestDataUser {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Booking booking;

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
        TicketType ticketType = TicketType.builder().title("Test").price(1000L).sectorType(sectorType).build();

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
            .address(address)
            .telephoneNumber(ADMIN_PHONE_NUMBER)
            .build();

        user = userRepository.save(user);

        Ticket ticket = Ticket.builder()
            .ticketType(ticketType)
            .seats(TICKET_SEATS)
            .owner(user)
            .performance(performance)
            .status(TICKET_STATUS_IN_CART)
            .updateDate(LocalDateTime.now())
            .build();

        ticket = ticketRepository.save(ticket);

        Set<Ticket> tickets = new HashSet<>();
        tickets.add(ticket);

        booking = Booking.builder()
            .buyDate(LocalDateTime.now())
            .user(user)
            .tickets(tickets)
            .invoice(null)
            .build();
    }

    @AfterEach
    public void afterEach () {
        bookingRepository.deleteAll();
        ticketRepository.deleteAll();
        performanceRepository.deleteAll();
        userRepository.deleteAll();
        artistRepository.deleteAll();
        addressRepository.deleteAll();
    }


    @Test
    @DisplayName("Should return correct entity back after create")
    public void whenCreateNew_thenGetCorrectEntityBack () {
        Booking newBooking = bookingRepository.save(booking);
        assertAll(
            () -> assertNotNull(booking.getId()),
            () -> assertEquals(newBooking.getBuyDate(), booking.getBuyDate()),
            () -> assertEquals(newBooking.getUser(), booking.getUser()),
            () -> assertEquals(newBooking.getTickets(), booking.getTickets()),
            () -> assertEquals(newBooking.getInvoice(), booking.getInvoice())
        );
    }
}
*/