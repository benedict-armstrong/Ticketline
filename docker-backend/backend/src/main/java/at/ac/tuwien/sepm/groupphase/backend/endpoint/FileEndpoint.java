package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.FileMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/files")
public class FileEndpoint {

    /*  ----- File Format Support -----
     *  Refer to the documentation of File.Type
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final FileService fileService;
    private final FileMapper fileMapper;

    @Autowired
    public FileEndpoint(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @PostMapping
    @Secured("ROLE_ORGANIZER")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload a file")
    public FileDto uploadFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("POST /files (size={}, type={})", file.getSize(), file.getContentType());
        return fileMapper.fileToFileDto(fileService.addFile(fileMapper.multipartFileToFile(file)));
    }

}
