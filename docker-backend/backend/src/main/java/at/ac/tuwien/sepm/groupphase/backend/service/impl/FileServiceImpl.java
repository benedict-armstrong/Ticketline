package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public File addFile(File file) {
        LOGGER.trace("save(file:(size={}, type={}))", file.getData().length, file.getType().toString());
        return fileRepository.save(file);
    }

    @Override
    public int getPdfCount() {
        return fileRepository.findAllByType(File.Type.APPLICATION_PDF).size();
    }

}
