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
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final BookingService bookingService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             UserService userService,
                             AuthenticationFacade authenticationFacade,
                             BookingService bookingService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.bookingService = bookingService;
    }

    @Override
    public List<Ticket> save(Performance performance, TicketType ticketType, Ticket.Status status, int amount) {
        LOGGER.trace("save({}, {},  {}, {})", performance, ticketType, status, amount);

        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());

        List<Ticket> ticketList = new LinkedList<>();
        Sector sector = ticketType.getSector();

        List<LayoutUnit> freeSeats = ticketRepository.getFreeSeatsInPerformanceAndSector(performance, sector);

        if (freeSeats.size() < amount) {
            if (amount > 1) {
                throw new NoTicketLeftException("Not enough free seats were found.");
            }
            throw new NoTicketLeftException("No free seat was found.");
        }

        for (int i = 0; i < amount; i++) {
            ticketList.add(
                Ticket.builder()
                    .ticketType(ticketType)
                    .performance(performance)
                    .status(status)
                    .user(user)
                    .changeDate(LocalDateTime.now())
                    .seat(freeSeats.get(i))
                    .build()
            );
        }

        return ticketRepository.saveAll(ticketList);
    }

    @Override
    public List<Ticket> getTickets(Ticket.Status status) {
        LOGGER.trace("getTickets({})", status);
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        List<Ticket> list = ticketRepository.findByUserAndStatus(user, status);
        List<Ticket> newList = new LinkedList<>();
        for (Ticket ticket : list) {
            Performance performance = ticket.getPerformance();
            Venue venue = performance.getVenue();
            List<Sector> sectorList = new LinkedList<>();
            for (Sector sector : venue.getSectors()) {
                boolean done = false;
                for (Sector newSector : sectorList) {
                    if (newSector.getId().equals(sector.getId())) {
                        done = true;
                        break;
                    }
                }
                if (!done) {
                    sectorList.add(sector);
                }
            }
            venue.setSectors(sectorList);
            performance.setVenue(venue);
            ticket.setPerformance(performance);
            newList.add(ticket);
        }
        return newList;
    }

    public List<LayoutUnit> getTakenSeatsInPerformance(Performance performance) {
        LOGGER.trace("getTakenSeatsInPerformance({})", performance);
        return ticketRepository.getTakenSeatsInPerformance(performance);
    }

    @Override
    public boolean checkout() {
        LOGGER.trace("checkout()");
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        List<Ticket> tickets = ticketRepository.findByUserAndStatus(user, Ticket.Status.IN_CART);
        if (tickets.size() < 1) {
            return false;
        }

        for (Ticket ticket : tickets) {
            ticket.setStatus(Ticket.Status.PAID_FOR);
        }
        ticketRepository.saveAll(tickets);
        bookingService.save(new HashSet<>(tickets), Booking.Status.PAID_FOR);
        return true;
    }

    @Override
    public boolean reserve() {
        LOGGER.trace("reserve()");
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        List<Ticket> tickets = ticketRepository.findByUserAndStatus(user, Ticket.Status.IN_CART);
        if (tickets.size() < 1) {
            return false;
        }

        for (Ticket ticket : tickets) {
            ticket.setStatus(Ticket.Status.RESERVED);
        }
        ticketRepository.saveAll(tickets);
        bookingService.save(new HashSet<>(tickets), Booking.Status.RESERVED);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        LOGGER.trace("delete({})", id);

        Optional<Ticket> optionalTicket = ticketRepository.findById(id);

        if (optionalTicket.isEmpty()) {
            return false;
        }
        ticketRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(List<Long> ids) {
        LOGGER.trace("delete({})", ids);
        List<Ticket> tickets = ticketRepository.findByIdList(ids);

        if (tickets.size() < 1) {
            return false;
        }

        ticketRepository.deleteAll(tickets);
        return true;
    }

    @Override
    @Scheduled(fixedDelay = 60000)
    public void pruneTickets() {
        LOGGER.trace("pruneTickets()");
        List<Ticket> toBeDeleted = ticketRepository.findByChangeDateBeforeAndStatus(LocalDateTime.now().minusMinutes(1), Ticket.Status.IN_CART);
        if (toBeDeleted.size() > 0) {
            LOGGER.info("Deleting {} stale tickets from carts", toBeDeleted.size());
            ticketRepository.deleteAll(toBeDeleted);
        }
    }

    @Override
    public void pruneReservations(List<Performance> performances) {
        LOGGER.trace("pruneReservations()");
        List<Ticket> tickets = new ArrayList();
        for (Performance performance : performances) {
            tickets.addAll(ticketRepository.findByPerformanceAndStatus(performance, Ticket.Status.RESERVED));
        }

        for (Ticket ticket : tickets) {
            bookingService.deleteTicket(ticket);
            ticketRepository.delete(ticket);
        }
    }

    @Override
    public void updateStatus(Set<Ticket> tickets, Ticket.Status status) {
        LOGGER.trace("updateStatus({}, {})", tickets, status);
        for (Ticket ticket : tickets) {
            ticket.setStatus(status);
        }

        ticketRepository.saveAll(tickets);
    }

    @Override
    @Transactional
    public void cancel(List<Long> ids) {
        LOGGER.trace("updateStatus({})", ids);
        List<Ticket> tickets = ticketRepository.findByIdList(ids);

        for (Ticket ticket : tickets) {
            ticket.setStatus(Ticket.Status.CANCELLED);
            ticketRepository.save(ticket);
        }

        if (tickets.size() != 0) {
            bookingService.checkIfAllTicketsCancelled(tickets.get(0));
        }
    }
}
