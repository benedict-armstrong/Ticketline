package at.ac.tuwien.sepm.groupphase.backend.basetest;

public interface TestDataNews extends TestData {

    Long ID = 1L;
    Long NEGATIVEID = -1L;
    String TEST_NEWS_TITLE = "TestNewsTitle";
    String TEST_NEWS_TEXT = "TestNewsText";

    String NEWS_BASE_URI = BASE_URI + "/news";

}
