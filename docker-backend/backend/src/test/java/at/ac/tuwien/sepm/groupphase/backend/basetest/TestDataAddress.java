package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;

public interface TestDataAddress extends TestData {
    String TEST_ADDRESS_NAME = "Testaddresse";
    String TEST_ADDRESS_LINE_ONE = "Line one";
    String TEST_ADDRESS_LINE_TWO = "Line two";
    String TEST_ADDRESS_CITY = "Testcity";
    String TEST_ADDRESS_CITY2 = "Linz";
    String TEST_ADDRESS_POSTCODE = "1000";
    String TEST_ADDRESS_COUNTRY = "Austria";

    String ADDRESS_BASE_URI = BASE_URI + "/addresses";

    static Address getAddress() {
        return Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .lineTwo(TEST_ADDRESS_LINE_TWO)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .build();
    }

}
