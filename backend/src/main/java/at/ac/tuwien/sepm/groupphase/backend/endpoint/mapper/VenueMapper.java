package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring", uses = {LayoutUnitMapper.class, AddressMapper.class, SectorMapper.class})
public interface VenueMapper {

    default Venue venueDtoToVenue(VenueDto venueDto) {
        if (venueDto == null) {
            return null;
        }

        int width = venueDto.getLayout().get(0).size();

        return Venue.builder()
            .id(venueDto.getId())
            .name(venueDto.getName())
            .address(AddressMapper.INSTANCE.addressDtoToAddress(venueDto.getAddress()))
            .sectors(SectorMapper.INSTANCE.sectorDtoListToSectorList(venueDto.getSectors()))
            .width(width)
            .layout(LayoutUnitMapper.INSTANCE.layoutUnitDtoMatrixToList(venueDto.getLayout()))
            .build();
    }

    default VenueDto venueToVenueDto(Venue venue) {
        if (venue == null) {
            return null;
        }

        return VenueDto.builder()
            .id(venue.getId())
            .name(venue.getName())
            .address(AddressMapper.INSTANCE.addressToAddressDto(venue.getAddress()))
            .sectors(SectorMapper.INSTANCE.sectorListToSectorDtoList(venue.getSectors()))
            .layout(LayoutUnitMapper.INSTANCE.layoutUnitListToLayoutUnitDtoMatrix(venue.getLayout(), venue.getWidth()))
            .build();

    }

    List<VenueDto> venueToVenueDto(List<Venue> venues);

}
