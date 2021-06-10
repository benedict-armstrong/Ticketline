package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {FileMapper.class, EventMapper.class})
public interface NewsMapper extends FileTypeMapper {

    News newsDtoToNews(NewsDto newsDto);

    NewsDto newsToNewsDto(News news);

    List<NewsDto> newsListToNewsDtoList(List<News> news);

    default Long map(EventDto value) {
        if (value == null) {
            return null;
        }
        return value.getId();
    }

    default EventDto map(Long value) {
        return EventDto.builder().id(value).build();
    }

}
