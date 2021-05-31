package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Profile("generateData")
@Component
@DependsOn({"venueDataGenerator", "fileDataGenerator", "artistDataGenerator"})
public class EventDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 5;

    private final EventRepository eventRepository;
    private final FileRepository fileRepository;
    private final ArtistRepository artistRepository;
    private final VenueRepository venueRepository;

    public EventDataGenerator(EventRepository eventRepository, FileRepository fileRepository,
                              ArtistRepository artistRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
        this.artistRepository = artistRepository;
        this.venueRepository = venueRepository;
    }

    private Performance buildPerformance(int i, Artist artist, Venue venue) {
        return Performance.builder()
            .title("Performance " + i)
            .description("This is a new performance part " + i)
            .date(LocalDateTime.of(2022, 12, 23, 15, 30).plusDays(i * 10L))
            .ticketTypes(buildTicketTypes())
            .artist(artist)
            .venue(venueRepository.findAll().get(0))
            .build();
    }

    private Set<TicketType> buildTicketTypes() {
        Set<TicketType> ticketTypes = new HashSet<>();
        ticketTypes.add(TicketType.builder().title("Standard").price(100).build());
        ticketTypes.add(TicketType.builder().title("VIP").price(150).build());
        ticketTypes.add(TicketType.builder().title("Discount").price(50).build());
        return ticketTypes;
    }

    @PostConstruct
    private void generateEvent() {
        if (eventRepository.findAll().size() > 0) {
            LOGGER.debug("Events have already been generated");
        } else {
            LOGGER.debug("Generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);

            List<Artist> artists = artistRepository.findAll();
            Set<File>    images  = fileRepository.findAll().stream()
                .filter(item -> item.getType() == File.Type.IMAGE_JPG || item.getType() == File.Type.IMAGE_PNG)
                .collect(Collectors.toSet());
            List<Venue>  venues  = venueRepository.findAll();

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {
                Set<Performance> performances = new HashSet<>();
                for (int j = 0; j < i; j++) {
                    Artist artist = artists.get((i+j) % artists.size());
                    Venue venue   = venues.get((i+j) % venues.size());
                    performances.add(buildPerformance(j, artist, venue));
                }
                Event event = Event.builder()
                    .name("Test Event " + i)
                    .description("This is a test description! Part " + i)
                    .eventType(Event.EventType.CONCERT)
                    .duration(100 + 50 * i)
                    .startDate(LocalDate.of(2021, 12, 25))
                    .endDate(LocalDate.of(2023, 12, 25))
                    .images(images)
                    .performances(performances)
                    .build();
                eventRepository.save(event);
            }
        }
    }

}
