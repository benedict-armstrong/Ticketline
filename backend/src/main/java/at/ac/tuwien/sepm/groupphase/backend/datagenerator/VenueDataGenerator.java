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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("generateData")
@Component
@DependsOn({"userDataGenerator"})
public class VenueDataGenerator {

    private static final int NUMBER_OF_VENUES_TO_GENERATE = 25;

    private static final int NUMBER_OF_SEATS = 500;
    private static final int NUMBER_OF_STANDING = 500;

    private final VenueService venueService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Autowired
    public VenueDataGenerator(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostConstruct
    private void generateVenue() {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin@mail.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        for (int i = 0; i < NUMBER_OF_VENUES_TO_GENERATE; i++) {

            Sector sectorStage = Sector.builder().localId(0L).name("SectorDto stage").color("#CCCCCC").type(Sector.SectorType.STAGE).build();
            Sector sectorSeated = Sector.builder().localId(1L).name("SectorDto seated").color("#EE9781").type(Sector.SectorType.SEATED).build();
            Sector sectorStanding = Sector.builder().localId(2L).name("SectorDto standing").color("#335F70").type(Sector.SectorType.STANDING).build();


            int venueWidth = 25;
            int size;

            List<LayoutUnit> venueLayout = new ArrayList<>();

            for (int j = 0; j < venueWidth * 2; j++) {
                venueLayout.add(LayoutUnit.builder().sector(sectorStage).localId(j).customLabel("" + j).build());
            }

            size = venueLayout.size();
            for (int j = size; j < NUMBER_OF_SEATS + size; j++) {
                venueLayout.add(LayoutUnit.builder().sector(sectorSeated).localId(j).customLabel("" + j).build());
            }

            size = venueLayout.size();
            for (int j = size; j < NUMBER_OF_STANDING + size; j++) {
                venueLayout.add(LayoutUnit.builder().sector(sectorStanding).localId(j).customLabel("" + j).build());
            }



            Faker faker = new Faker();

            String locationName = faker.address().streetName();
            String streetName = faker.address().streetName();
            String number = faker.address().buildingNumber();
            String city = faker.address().cityName();
            String country = faker.address().country();
            String postCode = faker.address().zipCode();

            venueService.add(
                Venue.builder()
                    .name("Venue " + i)
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
        }



        LOGGER.debug("generated Venues " + NUMBER_OF_VENUES_TO_GENERATE);

    }

}
