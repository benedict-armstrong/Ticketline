package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LayoutUnitRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Profile("generateData")
@Component
@DependsOn({"eventDataGenerator", "userDataGenerator"})
public class TicketDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static int NUMBER_OF_TICKETS_TO_GENERATE = 1000;

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        event.getApplicationContext().getBean(TicketDataGenerator.class).generateTickets();
    }

    public TicketDataGenerator(TicketRepository ticketRepository, EventRepository eventRepository,
                               UserRepository userRepository, BookingRepository bookingRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void generateTickets() {
        if (ticketRepository.findAll().size() >= NUMBER_OF_TICKETS_TO_GENERATE) {
            LOGGER.debug("Tickets have already been generated");
        } else {
            LOGGER.debug("Generating {} tickets", NUMBER_OF_TICKETS_TO_GENERATE);

            List<ApplicationUser> users = userRepository.findAll();
            List<Event> events = eventRepository.findAll();

            for (int i = 0; i < NUMBER_OF_TICKETS_TO_GENERATE; i++) {
                int numberOfUsers = users.size();
                ApplicationUser user = users.get(i % numberOfUsers);
                LOGGER.info("Generating ticket for {}", user.getFirstName());

                int numberOfEvents = events.size();
                Event event = events.get(i % numberOfEvents);
                Set<Performance> performances = event.getPerformances();
                int numberOfPerformances = performances.size();
                Performance performance = (Performance) performances.toArray()[i % numberOfPerformances];
                TicketType ticketType = (TicketType) performance.getTicketTypes().toArray()[0];

                if (NUMBER_OF_TICKETS_TO_GENERATE > performance.getVenue().getLayout().size()) {
                    NUMBER_OF_TICKETS_TO_GENERATE = performance.getVenue().getLayout().size();
                    LOGGER.info("Can only generate {} tickets due to limited amount of seats at venue", NUMBER_OF_TICKETS_TO_GENERATE);
                }

                LayoutUnit seat = performance.getVenue().getLayout().get(i);

                if (seat.getSector().getType().equals(Sector.SectorType.STAGE)) {
                    continue;
                }

                Random rand = new Random();

                Set<Ticket> ticketSet = new HashSet<>();

                ticketSet.add(Ticket.builder()
                    .ticketType(ticketType)
                    .performance(performance)
                    .changeDate(LocalDateTime.now().minusDays(i % 5 + rand.nextInt(7)).plusMinutes(rand.nextInt(100)))
                    .user(user)
                    .seat(seat)
                    .status(Ticket.Status.PAID_FOR)
                    .build()
                );

                Booking booking = Booking.builder()
                    .createDate(LocalDateTime.now())
                    .user(user)
                    .tickets(ticketSet)
                    .status(Booking.Status.PAID_FOR)
                    .invoice(null)
                    .build();

                ticketRepository.saveAll(ticketSet);
                bookingRepository.save(booking);
            }

        }
    }
}
