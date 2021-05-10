package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataNews;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NewsEndpointTest implements TestDataNews {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Event event = Event.EventBuilder.aEvent()
        .withTitle("Testevent")
        .build();

    private final News news = News.NewsBuilder.aNews()
        .withTitle(TEST_NEWS_TITLE)
        .withText(TEST_NEWS_TEXT)
        .withAuthor("Testuser")
        .withEvent(event)
        .withPublishedAt(TEST_NEWS_PUBLISHED_AT)
        .build();

    @BeforeEach
    public void beforeEach() {
        newsRepository.deleteAll();
        eventRepository.deleteAll();
        eventRepository.save(event);
    }

    @Test
    @DisplayName("Should return empty list when getting all news but there are none")
    public void givenNothing_whenGetAllNews_thenReturnEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<NewsDto> newsDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), NewsDto[].class)
        );
        assertEquals(0, newsDtos.size());
    }

    @Test
    @DisplayName("Should return 8 freshest news on get all with no arguments")
    public void givenManyNews_whenGetAllWithNoArguments_thenReturnListWith8FreshNews() throws Exception {
        LocalDateTime freshest = null;
        for (int i = 0; i < 15; i++) {
            News n = News.NewsBuilder.aNews()
                .withTitle(news.getTitle() + i)
                .withText(news.getText())
                .withAuthor(news.getAuthor())
                .withEvent(news.getEvent())
                .withPublishedAt(news.getPublishedAt().plusDays(i))
                .build();
            freshest = n.getPublishedAt();
            newsRepository.save(n);
        }
        assertNotNull(freshest);

        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        NewsDto[] newsDtos = objectMapper.readValue(response.getContentAsString(), NewsDto[].class);
        assertEquals(8, newsDtos.length);
        for (int i = 0; i < newsDtos.length; i++) {
            assertEquals(freshest.minusDays(i), newsDtos[i].getPublishedAt());
        }
    }

    @Test
    @DisplayName("Should return list of news with IDs smaller than offset on get all")
    public void givenNews_whenGetAllWithOnlyOffset_thenReturnListWithNews_AllIdsSmallerThanOffset() throws Exception {
        long limit = 4L;
        Long offset = null;
        for (int i = 0; i < limit + 4; i++) {
            News n = News.NewsBuilder.aNews()
                .withTitle(news.getTitle() + i)
                .withText(news.getText())
                .withAuthor(news.getAuthor())
                .withEvent(news.getEvent())
                .withPublishedAt(news.getPublishedAt().plusDays(i))
                .build();
            News saved = newsRepository.save(n);
            if (i == limit + 2) {
               offset = saved.getId();
            }
        }
        assertNotNull(offset);

        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI)
            .param("limit", Long.toString(limit))
            .param("offset", offset.toString())
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        NewsDto[] newsDtos = objectMapper.readValue(response.getContentAsString(), NewsDto[].class);
        assertEquals(limit, newsDtos.length);
        for (NewsDto newsDto : newsDtos) {
            assertTrue(newsDto.getId() < offset);
        }
    }

    @Test
    @DisplayName("Should return 422 on get all with negative limit")
    public void whenGetAllWithNegativeLimit_then422() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("limit", "-1")
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 422 on get all with non-numeric parameters")
    public void whenGetAllWithNonNumericParams_then422() throws Exception {
        MvcResult mvcResultL = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("limit", "A")
        ).andReturn();
        MvcResult mvcResultO = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("offset", "-")
        ).andReturn();
        MvcResult mvcResultLO = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("limit", "~")
                .param("offset", "B")
        ).andReturn();

        MockHttpServletResponse responseL = mvcResultL.getResponse();
        MockHttpServletResponse responseO = mvcResultO.getResponse();
        MockHttpServletResponse responseLO = mvcResultLO.getResponse();
        assertAll(
            () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseL.getStatus()),
            () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseO.getStatus()),
            () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseLO.getStatus())
        );
    }

}
