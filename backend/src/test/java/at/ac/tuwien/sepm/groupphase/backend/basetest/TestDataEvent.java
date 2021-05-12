package at.ac.tuwien.sepm.groupphase.backend.basetest;

import java.time.LocalDateTime;

public interface TestDataEvent extends TestData {

    Long ID = 1L;
    String TEST_EVENT_TITLE = "TestEventTitle";
    String TEST_EVENT_DESCRIPTION = "Testdescription..";
    LocalDateTime TEST_EVENT_DATE_FUTURE = LocalDateTime.parse("2022-12-12 22:00");
    LocalDateTime TEST_EVENT_DATE_PAST = LocalDateTime.parse("2000-12-12 12:00");
    int TEST_EVENT_DURATION = 100;


    String EVENT_BASE_URI = BASE_URI + "/event";

}
