package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import com.github.javafaker.Faker;
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

    private static final String SECTOR_1_NAME = "SECTOR 1";
    private static final String SECTOR_2_NAME = "SECTOR 1";

    private static final String SECTOR_1_TYPE = "SECTOR 1";
    private static final String SECTOR_2_TYPE = "SECTOR 1";

    private final VenueService venueService;

    private final UserRepository userRepository;

    //private final AuthenticationManager authManager;

    private final Sector sectorStage = Sector.builder().id(0L).name("SectorDto stage").color("#FFFCCC").type(Sector.SectorType.STAGE).build();
    private final Sector sectorSeated = Sector.builder().id(1L).name("SectorDto seated").color("#867FD2").type(Sector.SectorType.SEATED).build();
    private final Sector sectorStanding = Sector.builder().id(2L).name("SectorDto standing").color("#837F22").type(Sector.SectorType.STANDING).build();


    private final int venueWidth = 3;

    private final List<LayoutUnit> venueLayout = Arrays.asList(
        LayoutUnit.builder().sector(sectorStage).customLabel("1").build(),
        LayoutUnit.builder().sector(sectorStage).customLabel("2").build(),
        LayoutUnit.builder().sector(sectorStage).customLabel("3").build(),

        LayoutUnit.builder().sector(sectorSeated).customLabel("4").build(),
        null,
        LayoutUnit.builder().sector(sectorSeated).customLabel("6").build(),

        LayoutUnit.builder().sector(sectorStanding).customLabel("7").build(),
        LayoutUnit.builder().sector(sectorStanding).customLabel("8").build(),
        null
    );

    @Autowired
    public VenueDataGenerator(VenueService venueService, UserRepository userRepository/*, AuthenticationManager authManager*/) {
        this.venueService = venueService;
        this.userRepository = userRepository;
        //this.authManager = authManager;
    }

    @PostConstruct
    private void generateVenue() {

        UsernamePasswordAuthenticationToken authReq
            = new UsernamePasswordAuthenticationToken("admin@mail.com", "password");
        //Authentication auth = authManager.authenticate(authReq);
        //SecurityContext sc = SecurityContextHolder.getContext();
        //sc.setAuthentication(auth);

        Faker faker = new Faker();

        String locationName = faker.address().streetName();
        String streetName = faker.address().streetName();
        String number = faker.address().buildingNumber();
        String city = faker.address().cityName();
        String country = faker.address().country();
        String postCode = faker.address().zipCode();

        venueService.add(
            Venue.builder()
                .name("Venue")
                .sectors(Arrays.asList(sectorSeated, sectorStage, sectorStanding))
                .address(
                    Address.builder()
                        .name(locationName)
                        .lineOne(streetName + " " + number)
                        .city(city)
                        .postcode(postCode)
                        .country(country)
                        .eventLocation(true)
                        .build())
                .layout(venueLayout)
                .build()
        );
    }

}
