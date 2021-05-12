package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {

    Event eventDtoToEvent(EventDto eventDto);

    EventDto eventToEventDto(Event event);

    List<EventDto> eventToEventDto(List<Event> event);

}
