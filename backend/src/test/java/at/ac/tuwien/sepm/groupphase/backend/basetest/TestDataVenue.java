package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LayoutUnitDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface TestDataVenue extends TestData {

    String VENUE_BASE_URI = BASE_URI + "/venues";

    String VENUE_NAME = "Venue";
    String VENUE_ADDRESS_NAME = "Venue Address";
    String VENUE_ADDRESS_LINE_ONE = "Line one";
    String VENUE_ADDRESS_LINE_TWO = "Line two";
    String VENUE_ADDRESS_CITY = "City";
    String VENUE_ADDRESS_POSTCODE = "1010";
    String VENUE_ADDRESS_COUNTRY = "Austria";

    Sector SECTOR_STAGE = Sector.builder().localId(0L).name("SectorDto stage").color("#FFFCCC").type(Sector.SectorType.STAGE).build();
    Sector SECTOR_SEATED = Sector.builder().localId(1L).name("SectorDto seated").color("#867FD2").type(Sector.SectorType.SEATED).build();
    Sector SECTOR_STANDING = Sector.builder().localId(2L).name("SectorDto standing").color("#837F22").type(Sector.SectorType.STANDING).build();

    SectorDto SECTOR_DTO_STAGE = SectorDto.builder().id(0L).name("SectorDto stage").color("#FFFCCC").type(Sector.SectorType.STAGE).build();
    SectorDto SECTOR_DTO_SEATED = SectorDto.builder().id(1L).name("SectorDto seated").color("#867FD2").type(Sector.SectorType.SEATED).build();
    SectorDto SECTOR_DTO_STANDING = SectorDto.builder().id(2L).name("SectorDto standing").color("#837F22").type(Sector.SectorType.STANDING).build();

    List<List<LayoutUnitDto>> VENUE_LAYOUT_DTO = Arrays.asList(
        Arrays.asList(
            LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("1").build(),
            LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("2").build(),
            LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("3").build()
        ),
        Arrays.asList(
            LayoutUnitDto.builder().sector(SECTOR_DTO_SEATED).customLabel("4").build(),
            null,
            LayoutUnitDto.builder().sector(SECTOR_DTO_SEATED).customLabel("6").build()
        ),
        Arrays.asList(
            LayoutUnitDto.builder().sector(SECTOR_DTO_STANDING).customLabel("7").build(),
            LayoutUnitDto.builder().sector(SECTOR_DTO_STANDING).customLabel("8").build(),
            null
        )
    );

    int VENUE_WIDTH = 3;

    List<LayoutUnit> VENUE_LAYOUT = Arrays.asList(
        LayoutUnit.builder().sector(SECTOR_STAGE).customLabel("1").build(),
        LayoutUnit.builder().sector(SECTOR_STAGE).customLabel("2").build(),
        LayoutUnit.builder().sector(SECTOR_STAGE).customLabel("3").build(),
        LayoutUnit.builder().sector(SECTOR_SEATED).customLabel("4").build(),
        LayoutUnit.builder().sector(SECTOR_SEATED).customLabel("6").build(),
        LayoutUnit.builder().sector(SECTOR_STANDING).customLabel("7").build(),
        LayoutUnit.builder().sector(SECTOR_STANDING).customLabel("8").build()
    );

    static Venue getVenue() {
        Sector sector = getSeatedSector();
        return Venue.builder()
            .name(VENUE_NAME)
            .sectors(Collections.singletonList(sector))
            .address(
                Address.builder()
                    .name(VENUE_ADDRESS_NAME)
                    .lineOne(VENUE_ADDRESS_LINE_ONE)
                    .city(VENUE_ADDRESS_CITY)
                    .postcode(VENUE_ADDRESS_POSTCODE)
                    .country(VENUE_ADDRESS_COUNTRY)
                    .eventLocation(false)
                    .build())
            .layout(getVenueLayout(sector))
            .build();
    }

    static VenueDto getVenueDto() {
        return VenueDto.builder()
            .name(VENUE_NAME)
            .sectors(Arrays.asList(SECTOR_DTO_SEATED, SECTOR_DTO_STAGE, SECTOR_DTO_STANDING))
            .address(
                AddressDto.builder()
                    .name(VENUE_ADDRESS_NAME)
                    .lineOne(VENUE_ADDRESS_LINE_ONE)
                    .city(VENUE_ADDRESS_CITY)
                    .postcode(VENUE_ADDRESS_POSTCODE)
                    .country(VENUE_ADDRESS_COUNTRY)
                    .eventLocation(false)
                    .build())
            .layout(VENUE_LAYOUT_DTO)
            .build();
    }

    static Sector getSeatedSector() {
        return Sector.builder().name("SectorDto seated").color("#867FD2").type(Sector.SectorType.SEATED).build();
    }

    static SectorDto getSeatedSectorDto() {
        return SectorDto.builder().name("SectorDto seated").color("#867FD2").type(Sector.SectorType.SEATED).build();
    }

    static List<LayoutUnit> getVenueLayout(Sector sector) {
        return Arrays.asList(
            LayoutUnit.builder().sector(sector).customLabel("1").build(),
            LayoutUnit.builder().sector(sector).customLabel("2").build(),
            LayoutUnit.builder().sector(sector).customLabel("3").build(),
            LayoutUnit.builder().sector(sector).customLabel("11").build(),
            null,
            LayoutUnit.builder().sector(sector).customLabel("6").build(),
            LayoutUnit.builder().sector(sector).customLabel("7").build(),
            LayoutUnit.builder().sector(sector).customLabel("8").build(),
            null
        );
    }

    static List<List<LayoutUnitDto>> getVenueLayoutDto(SectorDto sectorDto) {
        return Arrays.asList(
            Arrays.asList(
                LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("1").build(),
                LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("2").build(),
                LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("3").build()
            ),
            Arrays.asList(
                LayoutUnitDto.builder().sector(SECTOR_DTO_SEATED).customLabel("4").build(),
                null,
                LayoutUnitDto.builder().sector(SECTOR_DTO_SEATED).customLabel("6").build()
            ),
            Arrays.asList(
                LayoutUnitDto.builder().sector(SECTOR_DTO_STANDING).customLabel("7").build(),
                LayoutUnitDto.builder().sector(SECTOR_DTO_STANDING).customLabel("8").build(),
                null
            )
        );
    }

}
