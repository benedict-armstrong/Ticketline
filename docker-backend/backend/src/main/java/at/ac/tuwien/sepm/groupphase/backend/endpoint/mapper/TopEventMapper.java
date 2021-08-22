package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopEventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.TopEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {EventMapper.class})
public interface TopEventMapper extends FileTypeMapper {
    TopEventDto topEventToTopEventDto(TopEvent topEvent);

    List<TopEventDto> topEventListToTopEventDtoList(List<TopEvent> topEvents);
}
