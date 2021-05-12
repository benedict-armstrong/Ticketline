package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataNews;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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
    private EventRepository eventRepository;

    private News news;
    private News news2;

    @BeforeEach
    public void beforeEach(){
        newsRepository.deleteAll();
        eventRepository.deleteAll();
        Event event = Event.EventBuilder.aEvent()
            .withTitle(TestDataEvent.TEST_EVENT_TITLE)
            .withDescription(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .withDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .withDuration(TestDataEvent.TEST_EVENT_DURATION)
            .withEventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();
        eventRepository.save(event);

        news = News.NewsBuilder.aNews()
            .withTitle(TEST_NEWS_TITLE)
            .withText(TEST_NEWS_TEXT)
            .withAuthor("Testuser")
            .withEvent(event)
            .withPublishedAt(TEST_NEWS_PUBLISHED_AT)
            .build();

        news2 = News.NewsBuilder.aNews()
            .withTitle(TEST_NEWS_TITLE + "2")
            .withText(TEST_NEWS_TEXT)
            .withAuthor("TestAuthor")
            .withEvent(event)
            .withPublishedAt(TEST_NEWS_PUBLISHED_AT)
            .build();
    }

    @Test
    @DisplayName("Check if one item is added")
    public void givenNothing_whenSaveNews_thenFindListWithOneElement() {
        newsRepository.save(news);

        assertAll(
            () -> assertEquals(1, newsRepository.findAll().size())
        );
    }

    @Test
    @DisplayName("Should return test news by id")
    public void givenNothing_whenSaveNews_thenFindNewsById() {
        newsRepository.save(news);

        assertAll(
            () -> assertNotNull(newsRepository.findById(news.getId()))
        );
    }

    @Test
    @DisplayName("Should not include news with ID when offset is equal to that ID")
    public void givenNews_whenOffsetIsEqualToId_thenExpectNoNewsWithIdInList() {
        News savedNews = newsRepository.save(news);
        assertFalse(newsRepository.getAll(10L, savedNews.getId()).contains(savedNews));
    }

    @Test
    @DisplayName("Should return list with all news when offset is larger than any ID")
    public void givenNews_whenGetAll_OffsetLargerThanAnyId_thenReturnEmptyList() {
        News savedNews1 = newsRepository.save(news);
        News savedNews2 = newsRepository.save(news2);
        List<News> allNews = newsRepository.getAll(10L, Long.MAX_VALUE);
        assertAll(
            () -> assertTrue(allNews.contains(savedNews1)),
            () -> assertTrue(allNews.contains(savedNews2))
        );
    }

    @Test
    @DisplayName("Should return empty list when getting all news with limit zero")
    public void givenNews_whenGetAll_LimitIsZero_thenReturnEmptyList() {
        newsRepository.save(news);
        assertTrue(newsRepository.getAll(0L, Long.MAX_VALUE).isEmpty());
    }

    @Test
    @DisplayName("Should return news with id")
    public void givenNews_whenFindOneByIdSameAsGiven() {
        News savedNews = newsRepository.save(news);
        assertEquals(newsRepository.findOneById(savedNews.getId()), savedNews);
    }

    @Test
    @DisplayName("Should return null when searching for negative id")
    public void givenNothing_whenFindOnyById_ShouldBeNull() {
        assertNull(newsRepository.findOneById(TestDataNews.NEGATIVEID));
    }
}
