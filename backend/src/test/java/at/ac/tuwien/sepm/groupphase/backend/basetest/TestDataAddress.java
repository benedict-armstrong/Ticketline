package at.ac.tuwien.sepm.groupphase.backend.basetest;

public interface TestDataAddress extends TestData {
    String TEST_ADDRESS_NAME = "Testaddresse";
    String TEST_ADDRESS_LINE_ONE = "Line one";
    String TEST_ADDRESS_LINE_TWO = "Line two";
    String TEST_ADDRESS_CITY = "Testcity";
    String TEST_ADDRESS_CITY2 = "Linz";
    String TEST_ADDRESS_POSTCODE = "1000";
    String TEST_ADDRESS_COUNTRY = "Austria";

    String ADDRESS_BASE_URI = BASE_URI + "/addresses";

}
