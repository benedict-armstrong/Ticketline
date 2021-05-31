package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.repository.CartItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
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
public class CartItemDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static int NUMBER_OF_CART_ITEMS_TO_GENERATE = 10;

    private final CartItemRepository cartItemRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public CartItemDataGenerator(CartItemRepository cartItemRepository, EventRepository eventRepository,
                                 UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void generateTickets() throws Exception {
        if (cartItemRepository.findAll().size() > 0) {
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

            for (int i = 0; i < NUMBER_OF_CART_ITEMS_TO_GENERATE; i++) {
                Set<Ticket> ticketSet = new HashSet<>();
                ticketSet.add(Ticket.builder()
                    .ticketType(ticketType)
                    .performance(performance)
                    .seat(performance.getVenue().getLayout().get(i))
                    .build()
                );

                CartItem cartItem = CartItem.builder()
                    .tickets(ticketSet)
                    .changeDate(LocalDateTime.now().plusMinutes(10))
                    .status(CartItem.Status.IN_CART)
                    .user(user)
                    .build();
                cartItemRepository.save(cartItem);
            }
        }
    }
}
