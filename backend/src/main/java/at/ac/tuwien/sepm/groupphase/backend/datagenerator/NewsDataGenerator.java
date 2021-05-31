package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Profile("generateData")
@Component
@DependsOn({"eventDataGenerator", "fileDataGenerator"})
public class NewsDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_NEWS_TO_GENERATE = 10;

    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;
    private final FileRepository fileRepository;

    public NewsDataGenerator(NewsRepository newsRepository, EventRepository eventRepository,
                             FileRepository fileRepository) {
        this.newsRepository = newsRepository;
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
    }

    @PostConstruct
    private void generateNews() {
        if (newsRepository.findAll().size() > 0) {
            LOGGER.debug("News have already been generated");
        } else {
            LOGGER.debug("Generating {} news entries", NUMBER_OF_NEWS_TO_GENERATE);

            List<Event> events = eventRepository.findAll();
            Set<File> images = fileRepository.findAll().stream()
                .filter(item -> item.getType() == File.Type.IMAGE_JPG || item.getType() == File.Type.IMAGE_PNG)
                .collect(Collectors.toSet());

            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                News news = News.builder()
                    .publishedAt(LocalDateTime.now())
                    .author("News Author " + i)
                    .title("News Title " + i)
                    .text("This is a News article " + i + " that was generated.")
                    .event(events.get(i % events.size()))
                    .build();
                if (i % (NUMBER_OF_NEWS_TO_GENERATE / 2) == 0) {
                    news.setImages(images);
                }
            }
        }
    }

    public static int getNumberOfNewsToGenerate() {
        return NUMBER_OF_NEWS_TO_GENERATE;
    }
}
