package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CustomImageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.CustomImage;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomImageMapper {

    CustomImage imgDtoToImg(CustomImageDto newsDto);

    List<CustomImage> imgDtoToImg(List<CustomImageDto> news);

    Set<CustomImage> imgDtoToImg(CustomImageDto[] news);

    CustomImageDto imgToImgDto(CustomImage news);

    List<CustomImageDto> imgToImgDto(List<CustomImage> news);

}
