package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecificationBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class EventRepositoryTest implements TestDataEvent {

    @Autowired
    EventRepository eventRepository;

    private Event event;

    @BeforeEach
    public void beforeEach(){

        event = Event.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .build();
    }

    @AfterEach
    public void afterEach() {
        eventRepository.deleteAll();
    }

    @Test
    @DisplayName("Check if one item is added")
    public void givenNothing_whenSaveEvent_thenFindListWithOneElement() {
        eventRepository.save(event);

        assertAll(
            () -> assertEquals(1, eventRepository.findAll().size())
        );
    }

    @Test
    @DisplayName("Should return test event by id")
    public void givenNothing_whenSaveEvent_thenFindEventById() {
        eventRepository.save(event);

        assertAll(
            () -> assertNotNull(eventRepository.findById(event.getId()))
        );
    }

    @Test
    @DisplayName("Should return null when searching for negative id")
    public void givenNothing_whenFindOneById_ShouldBeNull() {
        assertNull(eventRepository.findOneById(-1L));
    }

    @Test
    @DisplayName("Should return event when searching for event")
    public void givenEvent_whenSearch_ShouldReturnEvent() {
        eventRepository.save(event);

        EventSpecificationBuilder builder = new EventSpecificationBuilder();
        builder.with("name", ":", event.getName());

        assertEquals(event, eventRepository.findAll(builder.build()).get(0));
    }
}
