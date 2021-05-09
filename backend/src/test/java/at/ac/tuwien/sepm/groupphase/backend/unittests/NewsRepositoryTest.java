package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataNews;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class NewsRepositoryTest implements TestDataNews {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    EventRepository eventRepository;

    private News news;

    @BeforeEach
    public void beforeEach(){
        newsRepository.deleteAll();
        eventRepository.deleteAll();
        Event event = Event.EventBuilder.aEvent()
            .withTitle("Testevent")
            .build();
        eventRepository.save(event);

        news = News.NewsBuilder.aNews()
            .withTitle(TEST_NEWS_TITLE)
            .withText(TEST_NEWS_TEXT)
            .withAuthor("Testuser")
            .withEvent(event)
            .withPublishedAt(TEST_NEWS_PUBLISHED_AT)
            .build();
    }

    @Test
    public void givenNothing_whenSaveNews_thenFindListWithOneElementAndFindNewsById() {
        newsRepository.save(news);

        assertAll(
            () -> assertEquals(1, newsRepository.findAll().size()),
            () -> assertNotNull(newsRepository.findById(news.getId()))
        );
    }

}
