package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import org.mapstruct.Mapper;

@Mapper
public interface VenueMapper {

    Venue venueToVenueDto(VenueDto venueDto);

    VenueDto venueDtoToVenue(Venue venue);

}
