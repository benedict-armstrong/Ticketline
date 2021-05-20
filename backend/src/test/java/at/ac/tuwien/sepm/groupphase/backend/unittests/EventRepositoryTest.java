package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecificationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class EventRepositoryTest implements TestDataEvent {

    @Autowired
    EventRepository eventRepository;

    private Event event;

    @BeforeEach
    public void beforeEach(){
        eventRepository.deleteAll();

        event = Event.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .artist(TestDataEvent.getTestEventArtist())
            .location(TestDataEvent.getTestEventLocation())
            .sectorTypes(TestDataEvent.getTestEventSectortypes())
            .build();
    }

    @Test
    @DisplayName("Check if one item is added")
    public void givenNothing_whenSaveNews_thenFindListWithOneElement() {
        eventRepository.save(event);

        assertAll(
            () -> assertEquals(1, eventRepository.findAll().size())
        );
    }

    @Test
    @DisplayName("Should return test event by id")
    public void givenNothing_whenSaveNews_thenFindNewsById() {
        eventRepository.save(event);

        assertAll(
            () -> assertNotNull(eventRepository.findById(event.getId()))
        );
    }

    @Test
    @DisplayName("Should return null when searching for negative id")
    public void givenNothing_whenFindOnyById_ShouldBeNull() {
        assertNull(eventRepository.findOneById(-1L));
    }

    @Test
    @DisplayName("Should return event when searching for event")
    public void givenEvent_whenSearch_ShouldReturnEvent() {
        eventRepository.save(event);

        EventSpecificationBuilder builder = new EventSpecificationBuilder();
        builder.with("title", ":", event.getTitle());

        assertEquals(event, eventRepository.findAll(builder.build()).get(0));
    }
}
