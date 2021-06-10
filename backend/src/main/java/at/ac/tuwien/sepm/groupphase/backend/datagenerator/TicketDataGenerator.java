package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LayoutUnitRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Profile("generateData")
@Component
@DependsOn({"eventDataGenerator", "userDataGenerator"})
public class TicketDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static int NUMBER_OF_CART_ITEMS_TO_GENERATE = 10;

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LayoutUnitRepository layoutUnitRepository;

    public TicketDataGenerator(TicketRepository ticketRepository, EventRepository eventRepository,
                               UserRepository userRepository, LayoutUnitRepository layoutUnitRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.layoutUnitRepository = layoutUnitRepository;
    }

    @PostConstruct
    public void generateTickets() throws Exception {
        if (ticketRepository.findAll().size() > 0) {
            LOGGER.debug("Tickets have already been generated");
        } else {
            LOGGER.debug("Generating {} tickets", NUMBER_OF_CART_ITEMS_TO_GENERATE);

            ApplicationUser user = userRepository.findAll().get(0);
            Event event = eventRepository.findAll().get(0);
            Performance performance = (Performance) event.getPerformances().toArray()[0];
            TicketType ticketType = (TicketType) performance.getTicketTypes().toArray()[0];

            if (NUMBER_OF_CART_ITEMS_TO_GENERATE > performance.getVenue().getLayout().size()) {
                NUMBER_OF_CART_ITEMS_TO_GENERATE = performance.getVenue().getLayout().size();
                LOGGER.debug("Can only generate {} tickets due to limited amount of seats at venue", NUMBER_OF_CART_ITEMS_TO_GENERATE);
            }

            Set<Ticket> ticketSet = new HashSet<>();
            for (int i = 0; i < NUMBER_OF_CART_ITEMS_TO_GENERATE; i++) {
                LayoutUnit seat = performance.getVenue().getLayout().get(i);
                seat.setTaken(true);
                layoutUnitRepository.save(seat);
                ticketSet.add(Ticket.builder()
                    .ticketType(ticketType)
                    .performance(performance)
                    .changeDate(LocalDateTime.now().plusMinutes(100))
                    .user(user)
                    .seat(seat)
                    .status(Ticket.Status.IN_CART)
                    .build()
                );
            }
            ticketRepository.saveAll(ticketSet);
        }
    }
}
