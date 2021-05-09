package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FileDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import org.mapstruct.Mapper;

@Mapper
public interface FileMapper {

    FileDto fileToFileDto(File file);

    File fileDtoToFile(FileDto fileDto);

}
