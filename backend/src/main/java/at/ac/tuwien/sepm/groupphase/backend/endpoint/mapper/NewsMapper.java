package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface NewsMapper extends FileTypeMapper {

    News newsDtoToNews(NewsDto newsDto);

    NewsDto newsToNewsDto(News news);

    List<NewsDto> newsListToNewsDtoList(List<News> news);

}
