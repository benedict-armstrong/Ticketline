package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface ArtistMapper {

    @Mapping(target = "eventDtos", source = "events")
    ArtistDto artistToArtistDto(Artist artist);

    Artist artistDtoToArtist(ArtistDto artistDto);

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "artist", ignore = true)
    EventDto eventToEventDto(Event event);

    List<ArtistDto> artistListToArtistDtoList(List<Artist> artist);
}
