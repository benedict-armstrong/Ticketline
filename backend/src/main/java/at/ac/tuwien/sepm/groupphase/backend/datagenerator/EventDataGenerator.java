package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Profile("generateData")
@Component
public class EventDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 5;
    private static final String TEST_EVENT = "Test Event";
    private static final String TEST_EVENT_DESCRIPTION = "This is a test description! Part";
    private static final LocalDate TEST_EVENT_START_DATE = LocalDate.parse("2021-12-12");
    private static final LocalDate TEST_EVENT_END_DATE = LocalDate.parse("2023-12-12");
    private static final String TEST_PERFORMANCE_TITLE = "Performance";
    private static final String TEST_PERFORMANCE_DESCRIPTION = "THis is a new performance part";

    private static final String TEST_ARTIST_FIRSTNAME = "Artist";
    private static final String TEST_ARTIST_LASTNAME = "Famous";
    private static final String TEST_LOCATION_NAME = "Address";
    private static final String TEST_LOCATION_CITY = "Vienna";
    private static final String TEST_LOCATION_POSTCODE = "1000";
    private static final String TEST_LOCATION_COUNTRY = "Austria";
    private static final LocalDateTime TEST_DATETIME = LocalDateTime.parse("2022-12-12T22:00:00");

    private final EventRepository eventRepository;
    private final FileRepository fileRepository;
    private final AddressRepository addressRepository;
    private final ArtistRepository artistRepository;

    public EventDataGenerator(EventRepository eventRepository, FileRepository fileRepository, AddressRepository addressRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
        this.addressRepository = addressRepository;
        this.artistRepository = artistRepository;
    }

    @PostConstruct
    private void generateEvent() throws Exception {
        if (eventRepository.findAll().size() > 0) {
            LOGGER.debug("events already generated");
        } else {
            LOGGER.debug("generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);

            // using free images from pixabay to test
            // Image 1 https://pixabay.com/vectors/test-pattern-tv-tv-test-pattern-152459/
            // Image 2 https://cdn.pixabay.com/photo/2015/11/22/19/04/crowd-1056764_960_720.jpg
            // Image 3 https://pixabay.com/de/photos/hand-drehscheibe-dj-neon-lights-1850120/
            byte[] imgBuffer1 = recoverImageFromUrl("https://cdn.pixabay.com/photo/2013/07/12/17/47/test-pattern-152459_960_720.png");
            byte[] imgBuffer2 = recoverImageFromUrl("https://cdn.pixabay.com/photo/2015/11/22/19/04/crowd-1056764_960_720.jpg");
            byte[] imgBuffer3 = recoverImageFromUrl("https://cdn.pixabay.com/photo/2016/11/22/19/15/hand-1850120_960_720.jpg");

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {
                Set<File> set = new HashSet<>();
                File file = generateImage(imgBuffer1, File.Type.IMAGE_JPG);
                LOGGER.debug("saving file {} for event", file);
                fileRepository.save(file);
                set.add(file);
                file = generateImage(imgBuffer2, File.Type.IMAGE_JPEG);
                LOGGER.debug("saving file {} for event", file);
                fileRepository.save(file);
                set.add(file);
                file = generateImage(imgBuffer3, File.Type.IMAGE_PNG);
                LOGGER.debug("saving file {} for event", file);
                fileRepository.save(file);
                set.add(file);

                Set<Performance> performances = new HashSet<>();
                for (int j = 0; j <= i; j++) {
                    Address location = generateEventLocation(i);
                    addressRepository.save(location);

                    Artist artist = generateArtist(i);
                    artistRepository.save(artist);

                    Performance performance = Performance.builder()
                        .title(TEST_PERFORMANCE_TITLE + (j + i))
                        .description(TEST_PERFORMANCE_DESCRIPTION + (j + i))
                        .date(TEST_DATETIME.plusDays(i * 10))
                        .sectorTypes(generateSectorTypes(i))
                        .artist(artist)
                        .location(location)
                        .build();

                    performances.add(performance);
                }

                Event event = Event.builder()
                    .name(TEST_EVENT + i)
                    .description(TEST_EVENT_DESCRIPTION + i)
                    .eventType(Event.EventType.CONCERT)
                    .duration(100 + 50 * i)
                    .startDate(TEST_EVENT_START_DATE)
                    .endDate(TEST_EVENT_END_DATE)
                    .images(set)
                    .performances(performances)
                    .build();

                LOGGER.debug("saving event {}", event);
                eventRepository.save(event);
            }
        }
    }

    private byte[] recoverImageFromUrl(String urlText) throws Exception {
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }

    public static Artist generateArtist(int index) {
        return Artist.builder()
            .firstName(TEST_ARTIST_FIRSTNAME + index)
            .lastName(TEST_ARTIST_LASTNAME + index)
            .build();
    }

    public static Address generateEventLocation(int index) {
        return Address.builder().name(TEST_LOCATION_NAME + index)
            .lineOne("line " + index).city(TEST_LOCATION_CITY)
            .postcode(TEST_LOCATION_POSTCODE).country(TEST_LOCATION_COUNTRY).eventLocation(true).build();
    }

    public static Set<SectorType> generateSectorTypes(int index) {
        Set<SectorType> sectorTypes = new HashSet<>();
        sectorTypes.add(SectorType.builder().name("Standing").numberOfTickets(100 + index * 50).build());
        sectorTypes.add(SectorType.builder().name("Sitting").numberOfTickets(100 + index * 50).build());
        return sectorTypes;
    }

    public static File generateImage(byte[] imgBuffer, File.Type imageType) {
        File file = File.builder().data(imgBuffer).type(imageType).build();
        return file;
    }
}
