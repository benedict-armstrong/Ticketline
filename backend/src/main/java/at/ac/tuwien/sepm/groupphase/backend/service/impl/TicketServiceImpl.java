package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NoTicketLeftException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LayoutUnitRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@EnableScheduling
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final BookingService bookingService;
    private final LayoutUnitRepository layoutUnitRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             UserRepository userRepository,
                             AuthenticationFacade authenticationFacade,
                             BookingService bookingService,
                             LayoutUnitRepository layoutUnitRepository,
                             VenueRepository venueRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
        this.bookingService = bookingService;
        this.layoutUnitRepository = layoutUnitRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public List<Ticket> save(Performance performance, TicketType ticketType, Ticket.Status status, int amount) {
        LOGGER.trace("save({}, {},  {}, {})", performance, ticketType, status, amount);

        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());

        List<Ticket> ticketList = new LinkedList<>();
        Sector sector = ticketType.getSector();

        Optional<Venue> optVenue = venueRepository.findById(performance.getVenue().getId());
        if (optVenue.isEmpty()) {
            throw new NotFoundException("The performance you tried to buy a ticket for was not found.");
        }

        Venue venue = optVenue.get();
        for (int i = 0; i < amount; i++) {
            LayoutUnit seat = null;
            for (LayoutUnit spot : venue.getLayout()) {
                if (spot.getSector().getId().equals(sector.getId())) {
                    if (spot.getTaken() == null || !spot.getTaken()) {
                        seat = spot;
                        break;
                    }
                }
            }

            if (seat == null) {
                throw new NoTicketLeftException("No free seat was found.");
            }

            seat.setTaken(true);

            layoutUnitRepository.save(seat);

            ticketList.add(
                Ticket.builder()
                    .ticketType(ticketType)
                    .performance(performance)
                    .status(status)
                    .user(user)
                    .changeDate(LocalDateTime.now())
                    .seat(seat)
                    .build()
            );
        }

        return ticketRepository.saveAll(ticketList);
    }

    @Override
    public List<Ticket> getTickets(Ticket.Status status) {
        LOGGER.trace("getTickets({})", status);
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        return ticketRepository.findByUserAndStatus(user, status);
    }

    @Override
    public boolean checkout() {
        LOGGER.trace("checkout()");
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        List<Ticket> tickets = ticketRepository.findByUserAndStatus(user, Ticket.Status.IN_CART);
        if (tickets.size() > 0) {
            for (Ticket ticket : tickets) {
                ticket.setStatus(Ticket.Status.PAID_FOR);
            }
            ticketRepository.saveAll(tickets);
            bookingService.save(new HashSet<>(tickets));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        LOGGER.trace("delete({})", id);

        Optional<Ticket> optionalTicket = ticketRepository.findById(id);

        if (optionalTicket.isEmpty()) {
            return false;
        }

        Ticket ticket = optionalTicket.get();
        LayoutUnit seat = ticket.getSeat();
        seat.setTaken(false);
        layoutUnitRepository.save(seat);
        ticketRepository.deleteById(id);
        return true;
    }

    @Override
    @Scheduled(fixedDelay = 60000)
    public void pruneTickets() {
        LOGGER.trace("pruneCartItems()");
        List<Ticket> toBeDeleted = ticketRepository.findByChangeDateBeforeAndStatus(LocalDateTime.now().minusMinutes(1), Ticket.Status.IN_CART);
        if (toBeDeleted.size() > 0) {
            LOGGER.info("Deleting {} stale tickets from carts", toBeDeleted.size());
            Set<LayoutUnit> seats = new HashSet<>();
            for (Ticket ticket : toBeDeleted) {
                LayoutUnit seat = ticket.getSeat();
                seat.setTaken(false);
                seats.add(seat);
            }
            layoutUnitRepository.saveAll(seats);
        }
        ticketRepository.deleteAll(toBeDeleted);
    }
}
