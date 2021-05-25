package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataNews;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AddressRepository addressRepository;

    private News news;
    private News news2;

    @BeforeEach
    public void beforeEach(){
        //newsRepository.deleteAll();
        //eventRepository.deleteAll();

        Address address = addressRepository.save(TestDataEvent.TEST_EVENT_LOCATION);
        Artist artist = artistRepository.save(TestDataEvent.TEST_EVENT_ARTIST);

        Event event = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .build();
        eventRepository.save(event);

        news = News.builder()
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .author("Testuser")
            .event(event)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build();

        news2 = News.builder()
            .title(TEST_NEWS_TITLE + "2")
            .text(TEST_NEWS_TEXT)
            .author("TestAuthor")
            .event(event)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build();
    }

    @AfterEach
    public void afterEach() {
        newsRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        addressRepository.deleteAll();
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
    @DisplayName("Should return correct page of news")
    public void givenNews_whenGetAll_thenGetCorrectPage() {
        News savedNews1 = newsRepository.save(news);
        newsRepository.save(news2);
        List<News> allNews = newsRepository.findAllByOrderByPublishedAtDesc(PageRequest.of(1, 1)).getContent();
        assertAll(
            () -> assertEquals(1, allNews.size()),
            () -> assertEquals(savedNews1.getId(), allNews.get(0).getId())
        );
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
