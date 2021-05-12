package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
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

        event = Event.EventBuilder.aEvent()
            .withTitle(TEST_EVENT_TITLE)
            .withDescription(TEST_EVENT_DESCRIPTION)
            .withDate(TEST_EVENT_DATE)
            .withDuration(TEST_EVENT_DURATION)
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

}
