package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
@DependsOn({"eventDataGenerator", "userDataGenerator"})
public class TicketDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_TICKETS_TO_GENERATE = 10;

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TicketDataGenerator(TicketRepository ticketRepository, EventRepository eventRepository,
                               UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void generateTickets() throws Exception {
        if (ticketRepository.findAll().size() > 0) {
            LOGGER.debug("Tickets have already been generated");
        } else {
            LOGGER.debug("Generating {} tickets", NUMBER_OF_TICKETS_TO_GENERATE);

            ApplicationUser user = userRepository.findAll().get(0);
            Event event = eventRepository.findAll().get(0);
            Performance performance = (Performance) event.getPerformances().toArray()[0];
            TicketType ticketType = (TicketType) performance.getTicketTypes().toArray()[0];

            //for (int i = 0; i < NUMBER_OF_TICKETS_TO_GENERATE; i++) {
            //Ticket ticket = Ticket.builder()
            //    .owner(user)
            //    .performance(performance)
            //    .ticketType(ticketType)
            //    .build();
            //ticketRepository.save(ticket);
            //}
        }
    }
}
