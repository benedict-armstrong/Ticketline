package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
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
            userRepository.findUserByEmail(authenticationFacade.getMail())
        );
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket cancel(Long id) {
        LOGGER.trace("cancel({})", id);
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            ticket.setStatus(Ticket.Status.CANCELLED);
            return ticketRepository.save(ticket);
        }
        throw new NotFoundException(String.format("Could not find Ticket with id: %d", id));
    }

}
