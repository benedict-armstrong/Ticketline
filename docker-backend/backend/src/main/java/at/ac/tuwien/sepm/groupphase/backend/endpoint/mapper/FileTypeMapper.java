package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import org.mapstruct.Mapper;

/**
 * Provides mapper methods for the File.Type enumeration.
 * If your entity deals with File.Types directly, or contains another entity that has a File.Type property,
 * your entity's mapper interface must extend this interface.
 */
@Mapper
public interface FileTypeMapper {

    default File.Type map(String value) {
        return File.Type.fromMime(value);
    }

    default String map(File.Type value) {
        return value.toMime();
    }

}
