package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.FileMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.FileType;
import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/files")
public class FileEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final FileService fileService;
    private final FileMapper fileMapper;

    @Autowired
    public FileEndpoint(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileDto uploadFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("POST /files {}", file);
        if (file != null && file.getContentType() != null) {
            File fileEntity;
            try {
                fileEntity = new File(file.getBytes(), fileTypeFromMIME(file.getContentType()));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot retrieve bytes from file", e);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
            }
            return fileMapper.fileToFileDto(fileService.save(fileEntity));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file received");
        }
    }

    private FileType fileTypeFromMIME(String mime) throws IllegalArgumentException {
        // Add cases in the switch below (and in the enum FileType) to support other formats
        switch (mime) {
            case "image/jpg":
            case "image/jpeg":
                return FileType.IMAGE_JPEG;
            case "image/png":
                return FileType.IMAGE_PNG;
            default:
                throw new IllegalArgumentException("Format \"" + mime + "\" not supported");
        }
    }

}
