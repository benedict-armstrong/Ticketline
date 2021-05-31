package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class VenueDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TEST_VENUE_NAME = "Venue 1";

    private static final String TEST_VENUE_ADDRESS_NAME = "Address";
    private static final String TEST_VENUE_ADDRESS_LINE_1 = "Address";
    private static final String TEST_VENUE_ADDRESS_CITY = "Vienna";
    private static final String TEST_VENUE_ADDRESS_POSTCODE = "1010";
    private static final String TEST_VENUE_ADDRESS_COUNTRY = "Austria";

    private static final String SECTOR_1_NAME = "SECTOR 1";
    private static final String SECTOR_2_NAME = "SECTOR 1";

    private static final String SECTOR_1_TYPE = "SECTOR 1";
    private static final String SECTOR_2_TYPE = "SECTOR 1";

    private final VenueService venueService;

    @Autowired
    public VenueDataGenerator(VenueService venueService) {
        this.venueService = venueService;
    }



}
