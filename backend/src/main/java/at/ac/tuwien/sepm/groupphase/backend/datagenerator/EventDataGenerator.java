package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

            Faker faker = new Faker();

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {
                Set<Performance> performances = new HashSet<>();
                int venueOffset = 0;
                int artistOffset = 0;

                Date startDate = faker.date().future(800, TimeUnit.DAYS);
                Date endDate = faker.date().future(30, TimeUnit.DAYS, startDate);
                LocalDate eventStart = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate eventEnd = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                //generate 4 performances per event
                for (int j = 0; j < 4; j++) {
                    Artist artist = artists.get(artistOffset);
                    artistOffset = (artistOffset + 1) % artists.size();

                    Venue venue = venues.get(venueOffset);
                    venueOffset = (venueOffset + 1) % venues.size();

                    Performance performance = Performance.builder()
                        .title(faker.esports().event())
                        .description(faker.lorem().characters())
                        .date(faker.date().between(startDate, endDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .ticketTypes(buildTicketTypes())
                        .artist(artist)
                        .venue(venue)
                        .build();

                    performances.add(performance);
                }

                Event event = Event.builder()
                    .name("Event " + i)
                    .description(faker.lorem().characters())
                    .eventType(Event.EventType.CONCERT)
                    .duration(100 + 50 * i)
                    .startDate(eventStart)
                    .endDate(eventEnd)
                    .images(images)
                    .performances(performances)
                    .build();

                LOGGER.debug("saving event {}", event);
                eventRepository.save(event);
            }
        }
    }
}
