package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {FileMapper.class, PerformanceMapper.class, ArtistMapper.class, AddressMapper.class})
public interface EventMapper extends FileTypeMapper {
    Event eventDtoToEvent(EventDto eventDto);

    EventDto eventToEventDto(Event event);

    List<EventDto> eventListToEventDtoList(List<Event> events);
}
