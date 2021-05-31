package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

@Profile("generateData")
@Component
@DependsOn({"userDataGenerator"})
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

    private final UserRepository userRepository;

    private final AuthenticationManager authManager;

    private final Sector SECTOR_STAGE = Sector.builder().id(0L).name("SectorDto stage").color("#FFFCCC").type(Sector.SectorType.STAGE).build();
    private final Sector SECTOR_SEATED = Sector.builder().id(1L).name("SectorDto seated").color("#867FD2").type(Sector.SectorType.SEATED).build();
    private final Sector SECTOR_STANDING = Sector.builder().id(2L).name("SectorDto standing").color("#837F22").type(Sector.SectorType.STANDING).build();


    private final int VENUE_WIDTH = 3;

    private final List<LayoutUnit> VENUE_LAYOUT = Arrays.asList(
        LayoutUnit.builder().sector(SECTOR_STAGE).customLabel("1").build(),
        LayoutUnit.builder().sector(SECTOR_STAGE).customLabel("2").build(),
        LayoutUnit.builder().sector(SECTOR_STAGE).customLabel("3").build(),

        LayoutUnit.builder().sector(SECTOR_SEATED).customLabel("4").build(),
        null,
        LayoutUnit.builder().sector(SECTOR_SEATED).customLabel("6").build(),

        LayoutUnit.builder().sector(SECTOR_STANDING).customLabel("7").build(),
        LayoutUnit.builder().sector(SECTOR_STANDING).customLabel("8").build(),
        null
    );

    @Autowired
    public VenueDataGenerator(VenueService venueService, UserRepository userRepository, AuthenticationManager authManager) {
        this.venueService = venueService;
        this.userRepository = userRepository;
        this.authManager = authManager;
    }

    @PostConstruct
    private void generateVenue() {

        UsernamePasswordAuthenticationToken authReq
            = new UsernamePasswordAuthenticationToken("admin@mail.com", "password");
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        venueService.add(
            Venue.builder()
                .name("Venue")
                .sectors( Arrays.asList(SECTOR_SEATED, SECTOR_STAGE, SECTOR_STANDING))
                .address(
                    Address.builder()
                        .name(TEST_VENUE_ADDRESS_NAME)
                        .lineOne(TEST_VENUE_ADDRESS_LINE_1)
                        .city(TEST_VENUE_ADDRESS_CITY)
                        .postcode(TEST_VENUE_ADDRESS_POSTCODE)
                        .country(TEST_VENUE_ADDRESS_COUNTRY)
                        .eventLocation(false)
                        .build())
                .layout(VENUE_LAYOUT)
                .build()
        );
    }

}
