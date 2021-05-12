package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataFile;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
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

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        .withImages(images)
        .build();

    @BeforeEach
    public void beforeEach() {
        eventRepository.deleteAll();
        fileRepository.deleteAll();

        images = new HashSet<>();
        images.add(file);

        eventRepository.save(event);
        fileRepository.save(file);
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
