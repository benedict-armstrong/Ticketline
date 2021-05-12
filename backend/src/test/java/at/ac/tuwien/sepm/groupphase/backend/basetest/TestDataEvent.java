package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

import java.time.LocalDateTime;

public interface TestDataEvent extends TestData {

    Long ID = 1L;
    String TEST_EVENT_TITLE = "TestEventTitle";
    String TEST_EVENT_DESCRIPTION = "Testdescription..";
    LocalDateTime TEST_EVENT_DATE_FUTURE = LocalDateTime.parse("2022-12-12T22:00:00");
    LocalDateTime TEST_EVENT_DATE_PAST = LocalDateTime.parse("2000-12-12T12:00:00");
    Event.EventType TEST_EVENT_EVENT_TYPE = Event.EventType.CINEMA;
    int TEST_EVENT_DURATION = 100;


    String EVENT_BASE_URI = BASE_URI + "/events";

}
