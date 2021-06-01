package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {FileMapper.class, EventMapper.class})
public interface NewsMapper extends FileTypeMapper {

    News newsDtoToNews(NewsDto newsDto);

    @Mapping(target = "event", ignore = true)
    NewsDto newsToNewsDto(News news);

    List<NewsDto> newsListToNewsDtoList(List<News> news);

}
