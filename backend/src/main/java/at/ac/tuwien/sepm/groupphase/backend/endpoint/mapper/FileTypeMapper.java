package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.FileType;
import org.mapstruct.Mapper;

@Mapper
public interface FileTypeMapper {

    default FileType map(String value) {
        return FileType.fromMime(value);
    }

    default String map(FileType value) {
        return value.toMime();
    }

}
