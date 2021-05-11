package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.FileType;
import org.mapstruct.Mapper;

/**
 * Provides mapper methods for the FileType enumeration.
 *
 * If your entity deals with FileTypes directly, or contains another entity that has a FileType property,
 * your entity's mapper interface must extend this interface.
 */
@Mapper
public interface FileTypeMapper {

    default FileType map(String value) {
        return FileType.fromMime(value);
    }

    default String map(FileType value) {
        return value.toMime();
    }

}
