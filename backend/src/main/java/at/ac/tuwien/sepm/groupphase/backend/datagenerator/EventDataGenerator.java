package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

//import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
//import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class EventDataGenerator {
    // dummy generator for later events

    /*private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 5;
    private static final String TEST_EVENT = "Test Event";

    private final EventRepository eventRepository;

    public EventDataGenerator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void generateEvent() {
        if (eventRepository.findAll().size() > 0) {
            LOGGER.debug("events already generated");
        } else {
            LOGGER.debug("generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {
                Event event = Event.EventBuilder.aEvent().withTitle(TEST_EVENT + (i)).build();

                LOGGER.debug("saving event {}", event);
                eventRepository.save(event);
            }
        }
    }*/
}
