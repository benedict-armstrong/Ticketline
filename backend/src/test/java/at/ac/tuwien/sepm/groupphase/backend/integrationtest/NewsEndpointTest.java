package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataFile;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataNews;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NewsEndpointTest implements TestDataNews, TestDataFile {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Event event = Event.EventBuilder.aEvent()
        .withTitle(TestDataEvent.TEST_EVENT_TITLE)
        .withDescription(TestDataEvent.TEST_EVENT_DESCRIPTION)
        .withDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
        .withDuration(TestDataEvent.TEST_EVENT_DURATION)
        .withEventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
        .build();

    private final File file = File.FileBuilder.aFile()
        .withData(TEST_FILE_DATA)
        .withType(TEST_FILE_TYPE)
        .build();

    private Set<File> images = new HashSet<>();

    private final News news = News.NewsBuilder.aNews()
        .withTitle("Testtitle")
        .withText("Testtext")
        .withAuthor("Testuser")
        .withEvent(event)
        .withImages(images)
        .withPublishedAt(TEST_NEWS_PUBLISHED_AT)
        .build();

    @BeforeEach
    public void beforeEach() {
        newsRepository.deleteAll();
        eventRepository.deleteAll();
        fileRepository.deleteAll();

        images = new HashSet<>();
        images.add(file);

        eventRepository.save(event);
        fileRepository.save(file);
    }

    @Test
    @DisplayName("Should return empty list when getting all news but there are none")
    public void givenNothing_whenGetAllNews_thenReturnEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
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
    @DisplayName("Should return correctly sized chunk of news on get all")
    public void givenNews_whenGetAll_thenGetCorrectChunk() throws Exception {
        int amountOfNewsToGenerate = 15;
        int pageSize = 5;
        for (int i = 0; i < amountOfNewsToGenerate; i++) {
            News n = News.NewsBuilder.aNews()
                .withTitle(news.getTitle() + i)
                .withText(news.getText())
                .withAuthor(news.getAuthor())
                .withEvent(news.getEvent())
                .withPublishedAt(news.getPublishedAt())
                .build();
            newsRepository.save(n);
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI)
            .param("page", String.valueOf(1))
            .param("size", String.valueOf(pageSize))
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        NewsDto[] newsDtos = objectMapper.readValue(response.getContentAsString(), NewsDto[].class);
        assertEquals(pageSize, newsDtos.length);
    }

    @Test
    @DisplayName("Should return 422 on get all with negative parameters")
    public void whenGetAllWithNegativeLimit_then422() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("page", "-1")
                .param("size", "-2")
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 422 on get all with non-numeric parameters")
    public void whenGetAllWithNonNumericParams_then422() throws Exception {
        MvcResult mvcResultL = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("page", "A")
        ).andReturn();
        MvcResult mvcResultO = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("size", "-")
        ).andReturn();
        MvcResult mvcResultLO = this.mockMvc.perform(
            get(NEWS_BASE_URI)
                .param("page", "~")
                .param("size", "B")
        ).andReturn();

        MockHttpServletResponse responseL = mvcResultL.getResponse();
        MockHttpServletResponse responseO = mvcResultO.getResponse();
        MockHttpServletResponse responseLO = mvcResultLO.getResponse();
        assertAll(
            () -> assertEquals(HttpStatus.BAD_REQUEST.value(), responseL.getStatus()),
            () -> assertEquals(HttpStatus.BAD_REQUEST.value(), responseO.getStatus()),
            () -> assertEquals(HttpStatus.BAD_REQUEST.value(), responseLO.getStatus())
        );
    }

    @Test
    @DisplayName("Should return 201 and news object with set ID and publishing date after create")
    public void whenCreateNews_then201AndNewsWithIdAndPublishedAt() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(NEWS_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(news))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        NewsDto savedDto = objectMapper.readValue(response.getContentAsString(), NewsDto.class);
        assertAll(
            () -> assertNotNull(savedDto.getId()),
            () -> assertNotNull(savedDto.getPublishedAt())
        );
    }

    @Test
    @DisplayName("Should return 200 and news with the given ID on get by ID")
    public void givenNews_whenGetOneById_then200AndNewsWithId() throws Exception {
        News n = News.NewsBuilder.aNews()
            .withTitle(news.getTitle())
            .withText(news.getText())
            .withAuthor(news.getAuthor())
            .withEvent(news.getEvent())
            .withPublishedAt(news.getPublishedAt())
            .build();
        News saved = newsRepository.save(n);

        assertNotNull(saved);

        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + saved.getId())
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        NewsDto newsDto = objectMapper.readValue(response.getContentAsString(), NewsDto.class);

        assertEquals(newsDto.getId(), saved.getId());
    }

    @Test
    @DisplayName("Should return 404 when no news entry with given ID is found")
    public void whenGetOneByIdWithNegativeId_then404() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + -1)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}
