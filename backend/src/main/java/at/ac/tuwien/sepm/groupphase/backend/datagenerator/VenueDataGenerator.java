package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    private final VenueService venueService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Sector sectorStage = Sector.builder().localId(0L).name("SectorDto stage").color("#CCCCCC").type(Sector.SectorType.STAGE).build();
    private final Sector sectorSeated = Sector.builder().localId(1L).name("SectorDto seated").color("#867FD2").type(Sector.SectorType.SEATED).build();
    private final Sector sectorStanding = Sector.builder().localId(2L).name("SectorDto standing").color("#837F22").type(Sector.SectorType.STANDING).build();


    private final int venueWidth = 3;

    private final List<LayoutUnit> venueLayout = Arrays.asList(
        LayoutUnit.builder().sector(sectorStage).localId(0).customLabel("1").build(),
        LayoutUnit.builder().sector(sectorStage).localId(1).customLabel("2").build(),
        LayoutUnit.builder().sector(sectorStage).localId(2).customLabel("3").build(),
        LayoutUnit.builder().sector(sectorSeated).localId(3).customLabel("4").build(),
        LayoutUnit.builder().sector(sectorSeated).localId(5).customLabel("6").build(),
        LayoutUnit.builder().sector(sectorStanding).localId(6).customLabel("7").build(),
        LayoutUnit.builder().sector(sectorStanding).localId(7).customLabel("8").build()
    );

    @Autowired
    public VenueDataGenerator(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostConstruct
    private void generateVenue() {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin@mail.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

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
                .width(venueWidth)
                .build()
        );

        LOGGER.info("generated Venue");

    }

}
