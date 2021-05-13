package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataFile;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EventEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final File file = File.FileBuilder.aFile()
        .withData(TestDataFile.TEST_FILE_DATA)
        .withType(TestDataFile.TEST_FILE_TYPE)
        .build();

    private Set<File> images = new HashSet<>();

    private Event event = Event.EventBuilder.aEvent()
        .withTitle(TestDataEvent.TEST_EVENT_TITLE)
        .withDescription(TestDataEvent.TEST_EVENT_DESCRIPTION)
        .withDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
        .withDuration(TestDataEvent.TEST_EVENT_DURATION)
        .withEventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
        .withImages(images)
        .build();

    @BeforeEach
    public void beforeEach() {
        eventRepository.deleteAll();
        fileRepository.deleteAll();

        images = new HashSet<>();
        images.add(file);

        fileRepository.save(file);
    }

    @Test
    @DisplayName("Should return empty list when getting all news but there are none")
    public void givenNothing_whenGetAllNews_thenReturnEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "0")
                .param("size", "10")
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<EventDto> eventDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), EventDto[].class)
        );
        assertEquals(0, eventDtos.size());
    }

    @Test
    @DisplayName("Should return correctly sized chunk of events on get all")
    public void givenEvent_whenGetAll_thenGetCorrectChunk() throws Exception {
        int amountOfEventsToGenerate = 15;
        int pageSize = 5;
        for (int i = 0; i < amountOfEventsToGenerate; i++) {
            Event e = Event.EventBuilder.aEvent()
                .withTitle(TestDataEvent.TEST_EVENT_TITLE + i)
                .withDescription(TestDataEvent.TEST_EVENT_DESCRIPTION)
                .withDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
                .withDuration(TestDataEvent.TEST_EVENT_DURATION)
                .withEventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
                .build();
            eventRepository.save(e);
        }

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", String.valueOf(1))
                .param("size", String.valueOf(pageSize))
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        EventDto[] eventDtos = objectMapper.readValue(response.getContentAsString(), EventDto[].class);
        assertEquals(pageSize, eventDtos.length);
    }

    @Test
    @DisplayName("Should return 422 on get all with negative parameters")
    public void whenGetAllWithNegativeLimit_then422() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
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
            get(TestDataEvent.EVENT_BASE_URI)
                .param("page", "A")
        ).andReturn();
        MvcResult mvcResultO = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .param("size", "-")
        ).andReturn();
        MvcResult mvcResultLO = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
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
    @DisplayName("Should return 201 and news object with set ID")
    public void whenCreateEvent_then201AndEventWithIdAndPublishedAt() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        EventDto savedDto = objectMapper.readValue(response.getContentAsString(), EventDto.class);
        assertAll(
            () -> assertNotNull(savedDto.getId())
        );
    }


    @Test
    @DisplayName("Should return 400 when Date is in past")
    public void whenCreateEventWithPastDate_then400() throws Exception {
        Event invalidEvent = Event.EventBuilder.aEvent()
            .withTitle(TestDataEvent.TEST_EVENT_TITLE)
            .withDescription(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .withDate(TestDataEvent.TEST_EVENT_DATE_PAST)
            .withDuration(TestDataEvent.TEST_EVENT_DURATION)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEvent))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
