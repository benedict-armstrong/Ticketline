package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Profile("generateData")
@Component
public class NewsDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_NEWS_TO_GENERATE = 5;
    private static final String TEST_AUTHOR_NAME = "Test Author";
    private static final String TEST_TITLE = "Test Title";
    private static final String TEST_TEXT = "Test Test Test";
    private static final String TEST_EVENT = "Testevent";
    private static final String TEST_EVENT_DESCRIPTION = "This is a test description! Part";
    private static final LocalDateTime TEST_DATE = LocalDateTime.parse("2025-06-30T12:00:00");

    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;
    private final FileRepository fileRepository;
    private final ArtistRepository artistRepository;
    private final AddressRepository addressRepository;

    public NewsDataGenerator(NewsRepository newsRepository, FileRepository fileRepository, EventRepository eventRepository, ArtistRepository artistRepository, AddressRepository addressRepository) {
        this.newsRepository = newsRepository;
        this.fileRepository = fileRepository;
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
        this.addressRepository = addressRepository;
    }

    @PostConstruct
    private void generateNewsWithoutPictures() throws Exception {
        if (newsRepository.findAll().size() > 0) {
            LOGGER.debug("news already generated");
        } else {
            List<Event> events;

            if (eventRepository.findAll().size() > 4) {
                LOGGER.debug("events for news already generated");
                events = eventRepository.findAll();
            } else {
                LOGGER.debug("generating {} event entries for news", NUMBER_OF_NEWS_TO_GENERATE);

                events = this.generateEventsForNews();
            }

            LOGGER.debug("generating {} news entries with events and without pictures", NUMBER_OF_NEWS_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                News news = News.builder().publishedAt(LocalDateTime.now()).author(TEST_AUTHOR_NAME)
                    .title(TEST_TITLE + i).text(TEST_TEXT + i).event(events.get(i)).build();

                LOGGER.debug("saving news {}", news);
                newsRepository.save(news);
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

    @PostConstruct
    private void generateNewsWithEventWithPictures() throws Exception {
        if (newsRepository.findAll().size() > 5) {
            LOGGER.debug("news already generated");
        } else {
            List<Event> events;

            if (eventRepository.findAll().size() > 4) {
                LOGGER.debug("events for news already generated");
                events = eventRepository.findAll();
            } else {
                LOGGER.debug("generating {} event entries for news", NUMBER_OF_NEWS_TO_GENERATE);

                events = this.generateEventsForNews();
            }

            LOGGER.debug("generating {} news entries with events and with pictures", NUMBER_OF_NEWS_TO_GENERATE);

            // using free images from pixabay to test
            // Image 1 https://pixabay.com/vectors/test-pattern-tv-tv-test-pattern-152459/
            // Image 2 https://cdn.pixabay.com/photo/2015/11/22/19/04/crowd-1056764_960_720.jpg
            // Image 3 https://pixabay.com/de/photos/hand-drehscheibe-dj-neon-lights-1850120/
            byte[] imgBuffer1 = recoverImageFromUrl("https://cdn.pixabay.com/photo/2013/07/12/17/47/test-pattern-152459_960_720.png");
            byte[] imgBuffer2 = recoverImageFromUrl("https://cdn.pixabay.com/photo/2015/11/22/19/04/crowd-1056764_960_720.jpg");
            byte[] imgBuffer3 = recoverImageFromUrl("https://cdn.pixabay.com/photo/2016/11/22/19/15/hand-1850120_960_720.jpg");

            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                Set<File> set = new HashSet<>();
                File file1 = File.builder().data(imgBuffer1).type(File.Type.IMAGE_PNG).build();
                set.add(file1);
                LOGGER.debug("saving file {} for news", file1);
                fileRepository.save(file1);

                File file2 = File.builder().data(imgBuffer2).type(File.Type.IMAGE_JPG).build();
                set.add(file2);
                LOGGER.debug("saving file {} for news", file2);
                fileRepository.save(file2);

                File file3 = File.builder().data(imgBuffer3).type(File.Type.IMAGE_JPG).build();
                set.add(file3);
                LOGGER.debug("saving file {} for news", file3);
                fileRepository.save(file3);

                News news = News.builder().publishedAt(LocalDateTime.now()).author(TEST_AUTHOR_NAME)
                    .title(TEST_TITLE + (i + 10)).text(TEST_TEXT + (i + 10)).event(events.get(i))
                    .images(set).build();

                LOGGER.debug("saving news {}", news);
                newsRepository.save(news);
            }

        }
    }

    private List<Event> generateEventsForNews() throws Exception {
        List<Event> events = new LinkedList<>();
        byte[] imgBuffer1 = recoverImageFromUrl("https://cdn.pixabay.com/photo/2013/07/12/17/47/test-pattern-152459_960_720.png");

        for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
            Set<File> set = new HashSet<>();
            File file = EventDataGenerator.generateImage(imgBuffer1, File.Type.IMAGE_JPG);
            LOGGER.debug("saving file {} for event for news", file);
            fileRepository.save(file);
            set.add(file);

            Address location = EventDataGenerator.generateEventLocation(i);
            addressRepository.save(location);

            Artist artist = EventDataGenerator.generateArtist(i);
            artistRepository.save(artist);

            Set<Performance> performances = new HashSet<>();
            Performance performance = Performance.builder()
                .title(TEST_EVENT + (i))
                .description(TEST_EVENT_DESCRIPTION + (i))
                .date(TEST_DATE.plusDays(i * 10))
                .sectorTypes(EventDataGenerator.generateSectorTypes(i))
                .artist(artist)
                .location(location)
                .build();

            performances.add(performance);

            Event event = Event.builder()
                .name(TEST_EVENT + i)
                .description(TEST_EVENT_DESCRIPTION + (i))
                .startDate(TEST_DATE.plusDays(i * 10))
                .endDate(TEST_DATE.plusDays(i * 20))
                .eventType(Event.EventType.CONCERT)
                .duration(100 + i * 50)
                .images(set)
                .performances(performances)
                .build();

            events.add(event);

            LOGGER.debug("saving event {} for news", performance);
            eventRepository.save(event);
        }

        return events;
    }

}
