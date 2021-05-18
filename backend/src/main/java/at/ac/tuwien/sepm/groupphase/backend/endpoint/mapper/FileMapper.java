package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FileDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.exception.BadFileException;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper
public interface FileMapper extends FileTypeMapper {

    FileDto fileToFileDto(File file);

    File fileDtoToFile(FileDto fileDto);

    /**
     * Maps a MultipartFile object to a File entity.
     *
     * @param multipartFile the MultipartFile object to be mapped
     * @return the corresponding File object
     * @throws BadFileException if file cannot be read or is unsupported
     */
    default File multipartFileToFile(MultipartFile multipartFile) throws BadFileException {
        File file;
        if (multipartFile.getContentType() == null) {
            throw new BadFileException("The file is missing content type");
        }
        try {
            file = File.builder()
                .data(multipartFile.getBytes())
                .type(File.Type.fromMime(multipartFile.getContentType()))
                .build();
        } catch (IOException e) {
            throw new BadFileException("Couldn't retrieve bytes from file", e);
        } catch (IllegalArgumentException e) {
            throw new BadFileException("File format \"" + multipartFile.getContentType() + "\" is not supported", e);
        }
        return file;
    }

}
