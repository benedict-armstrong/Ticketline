package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLConnection;
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
    private static final String TEST_EVENT = "Test Event for News";

    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;

    public NewsDataGenerator(NewsRepository newsRepository, EventRepository eventRepository) {
        this.newsRepository = newsRepository;
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void generateNewsWithEventWithoutPictures() {
        if (newsRepository.findAll().size() > 0) {
            LOGGER.debug("news already generated");
        } else {
            List<Event> events = new LinkedList<>();

            if (eventRepository.findAll().size() > 4) {
                LOGGER.debug("events for news already generated");
                events = eventRepository.findAll();
            } else {
                LOGGER.debug("generating {} event entries for news", NUMBER_OF_NEWS_TO_GENERATE);

                for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                    Event event = Event.EventBuilder.aEvent().withTitle(TEST_EVENT + (i)).build();

                    events.add(event);

                    LOGGER.debug("saving event {} for news", event);
                    eventRepository.save(event);
                }
            }

            LOGGER.debug("generating {} news entries with events and without pictures", NUMBER_OF_NEWS_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                News news = News.NewsBuilder.aNews().withPublishedAt(LocalDateTime.now()).withAuthor(TEST_AUTHOR_NAME)
                    .withTitle(TEST_TITLE + i).withText(TEST_TEXT + i).withEvent(events.get(i)).build();

                LOGGER.debug("saving news {}", news);
                newsRepository.save(news);
            }
        }
    }


    /*@PostConstruct
    private void generateNewsWithEventWithPictures() {
        if (newsRepository.findAll().size() > 5) {
            LOGGER.debug("news already generated");
        } else {
            List<Event> events = new LinkedList<>();

            if (eventRepository.findAll().size() > 4) {
                LOGGER.debug("events for news already generated");
                events = eventRepository.findAll();
            } else {
                LOGGER.debug("generating {} event entries for news", NUMBER_OF_NEWS_TO_GENERATE);

                for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                    Event event = Event.EventBuilder.aEvent().withTitle(TEST_EVENT + (i)).build();

                    events.add(event);

                    LOGGER.debug("saving event {} for news", event);
                    eventRepository.save(event);
                }
            }

            LOGGER.debug("generating {} news entries with events and with pictures", NUMBER_OF_NEWS_TO_GENERATE);

            byte[] buffer = null;
            try {
                URL url = new URL("https://picjumbo.com/wp-content/uploads/the-golden-gate-bridge-sunset-1080x720.jpg");
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                StringBuffer sb = new StringBuffer();
                int r;
                while ((r = isr.read()) != -1) {
                    sb.append(r);
                }

                buffer = sb.toString().getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }



            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                Set<File> set = new HashSet<>();
                set.add(File.FileBuilder.aFile().withData(buffer).withType(File.Type.IMAGE_JPG).build());

                News news = News.NewsBuilder.aNews().withPublishedAt(LocalDateTime.now()).withAuthor(TEST_AUTHOR_NAME)
                    .withTitle(TEST_TITLE + (i + 10)).withText(TEST_TEXT + (i + 10)).withEvent(events.get(i))
                    .withImages(set).build();

                LOGGER.debug("saving news {}", news);
                newsRepository.save(news);
            }

        }
    }*/

}
