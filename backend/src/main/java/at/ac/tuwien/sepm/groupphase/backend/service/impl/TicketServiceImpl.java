package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             UserRepository userRepository,
                             AuthenticationFacade authenticationFacade) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
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
    public List<Ticket> getCartTickets() {
        LOGGER.trace("getCartTickets()");
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        Ticket.Status status = Ticket.Status.IN_CART;
        return ticketRepository.findByOwnerAndStatus(user, status);
    }

    @Override
    public TicketUpdateDto updateSeats(TicketUpdateDto ticketUpdate) {
        LOGGER.trace("updateSeats({})", ticketUpdate);
        Ticket ticket = ticketRepository.findById(ticketUpdate.getId()).get();
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
}
