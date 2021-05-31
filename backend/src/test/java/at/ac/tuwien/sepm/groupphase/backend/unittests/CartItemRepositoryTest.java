package at.ac.tuwien.sepm.groupphase.backend.unittests;

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
public class CartItemRepositoryTest implements TestDataUser, TestDataVenue {
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

    private CartItem cartItem;

    private ApplicationUser savedUser;

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

        savedUser = userRepository.save(ApplicationUser.builder()
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
            .build()
        );

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
        LayoutUnit layoutUnit = LayoutUnit.builder()
            .customLabel("Yes")
            .sector(sector)
            .localId(1)
            .build();
        layoutUnitList.add(layoutUnit);

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
            .seat(layoutUnit)
            .build();
        Set<Ticket> ticketSet = new HashSet<>();
        ticketSet.add(ticket1);

        cartItem = CartItem.builder()
            .status(CartItem.Status.IN_CART)
            .changeDate(LocalDateTime.now())
            .tickets(ticketSet)
            .user(savedUser)
            .build();
    }

    @AfterEach
    public void afterEach () {
        cartItemRepository.deleteAll();
        performanceRepository.deleteAll();
        venueRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return correct entity back after create")
    public void whenCreateNew_thenGetCorrectEntityBack() {
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        assertAll(
            () -> assertNotNull(savedCartItem.getId()),
            () -> assertEquals(cartItem.getUser(), savedCartItem.getUser()),
            () -> assertEquals(cartItem.getStatus(), savedCartItem.getStatus()),
            () -> assertEquals(cartItem.getTickets().size(), savedCartItem.getTickets().size())
        );
    }
}
