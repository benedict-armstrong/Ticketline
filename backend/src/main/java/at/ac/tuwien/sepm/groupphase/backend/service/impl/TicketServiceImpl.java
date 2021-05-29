package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NoTicketLeftException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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
import java.util.List;

@Service
@EnableScheduling
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final BookingService bookingService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             UserRepository userRepository,
                             AuthenticationFacade authenticationFacade,
                             BookingService bookingService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
        this.bookingService = bookingService;
    }

    @Override
    public Ticket save(Ticket ticket, Ticket.Status status) {
        LOGGER.trace("saveTicket({})", ticket);
        ticket.setOwner(
            userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal())
        );
        ticket.setStatus(status);
        ticket.setUpdateDate(LocalDateTime.now());


        List<Ticket> existingTicket = ticketRepository.findByOwnerAndTicketTypeAndStatus(ticket.getOwner(), ticket.getTicketType(), ticket.getStatus());
        if (existingTicket.size() > 0) {
            ticketRepository.deleteAll(existingTicket);
        }

        Long seatsNeeded = 0L;
        for (Long seat : ticket.getSeats()) {
            seatsNeeded += seat;
        }

        Long seatsLeft = ticket.getTicketType().getSectorType().getNumberOfTickets() - this.ticketsInUse(ticket.getPerformance(), ticket.getTicketType().getSectorType());

        if ((seatsLeft - seatsNeeded) < 0) {
            throw new NoTicketLeftException("Could not process request, there are only " + seatsLeft + " tickets remaining.");
        }
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket cancel(Long id) {
        LOGGER.trace("cancel({})", id);
        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setStatus(Ticket.Status.CANCELLED);
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getTickets(Ticket.Status status) {
        LOGGER.trace("getTickets({})", status);
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        return ticketRepository.findByOwnerAndStatus(user, status);
    }

    @Override
    public TicketUpdateDto updateSeats(TicketUpdateDto ticketUpdate) {
        LOGGER.trace("updateSeats({})", ticketUpdate);
        Ticket ticket = ticketRepository.findById(ticketUpdate.getId()).get();
        ticket.setUpdateDate(LocalDateTime.now());
        Long oldSeats = 0L;
        Long newSeats = 0L;
        for (Long seat : ticket.getSeats()) {
            oldSeats += seat;
        }
        for (Long seat : ticketUpdate.getSeats()) {
            newSeats += seat;
        }
        if (oldSeats < newSeats) {
            Long seatsNeeded = newSeats - oldSeats;
            System.out.println(seatsNeeded);
            Long seatsLeft = ticket.getTicketType().getSectorType().getNumberOfTickets() - this.ticketsInUse(ticket.getPerformance(), ticket.getTicketType().getSectorType());

            if ((seatsLeft - seatsNeeded) < 0) {
                ticketRepository.save(ticket);
                throw new NoTicketLeftException("Could not process request, there are only " + seatsLeft + " tickets remaining.");
            }
        }
        ticket.setSeats(ticketUpdate.getSeats());
        ticketRepository.save(ticket);
        return TicketUpdateDto.builder().id(ticket.getId()).seats(ticket.getSeats()).build();
    }

    @Override
    public boolean delete(Long id) {
        LOGGER.trace("delete({})", id);
        ticketRepository.deleteById(id);
        return true;
    }

    @Scheduled(fixedDelay = 60000)
    public void pruneTicketsInCart() {
        LOGGER.trace("pruneTicketsInCart()");
        List<Ticket> toBeDeleted = ticketRepository.findByUpdateDateBeforeAndStatus(LocalDateTime.now().minusMinutes(1), Ticket.Status.IN_CART);
        if (toBeDeleted.size() > 0) {
            LOGGER.info("Deleting {} stale tickets from carts", toBeDeleted.size());
        }
        ticketRepository.deleteAll(toBeDeleted);
    }

    @Override
    public boolean checkout() {
        LOGGER.trace("checkout()");
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        List<Ticket> tickets = ticketRepository.findByOwnerAndStatus(user, Ticket.Status.IN_CART);
        if (tickets.size() > 0) {
            for (Ticket ticket : tickets) {
                ticket.setStatus(Ticket.Status.PAID_FOR);
            }
            ticketRepository.saveAll(tickets);

            Booking booking = Booking.builder().tickets(new HashSet<>(tickets)).build();
            bookingService.save(booking);
            return true;
        } else {
            return false;
        }
    }

    private Long ticketsInUse(Performance performance, SectorType sectorType) {
        List<Ticket> currentlyInUse = ticketRepository.findByPerformanceAndTicketTypeSectorTypeAndStatusNot(performance, sectorType, Ticket.Status.CANCELLED);
        Long inUse = 0L;
        for (Ticket item : currentlyInUse) {
            for (Long seat : item.getSeats()) {
                inUse += seat;
            }
        }
        return inUse;
    }
}
