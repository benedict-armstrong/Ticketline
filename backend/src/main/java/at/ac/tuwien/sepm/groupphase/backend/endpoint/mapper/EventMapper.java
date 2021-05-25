package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface EventMapper extends FileTypeMapper {
    Event eventDtoToEvent(EventDto eventDto);

    EventDto eventToEventDto(Event event);

    List<EventDto> eventListToEventDtoList(List<Event> events);

    @Mapping(target = "performances", ignore = true)
    ArtistDto artistToArtistDto(Artist artist);

    @Mapping(target = "performances", ignore = true)
    AddressDto addressToAddressDto(Address address);
}
