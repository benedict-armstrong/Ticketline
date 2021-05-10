package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.FileType;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Profile("generateData")
@Component
public class FileDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_FILES_TO_GENERATE = 5;
    private static final int MAXIMUM_LENGTH_OF_FILE_DATA = 50;

    private final FileRepository fileRepository;

    public FileDataGenerator(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostConstruct
    private void generateFiles() {
        if (fileRepository.findAll().size() > 0) {
            LOGGER.debug("Files have already been generated");
        } else {
            LOGGER.debug("Generating {} files", NUMBER_OF_FILES_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_FILES_TO_GENERATE; i++) {
                File file = File.FileBuilder.aFile()
                    .withData(randomData())
                    .withType(randomFileType())
                    .build();
                LOGGER.debug("Saving file {}", file);
                fileRepository.save(file);
            }
        }
    }

    private byte[] randomData() {
        byte[] data = new byte[ThreadLocalRandom.current().nextInt(MAXIMUM_LENGTH_OF_FILE_DATA)];
        new Random().nextBytes(data);
        return data;
    }

    private FileType randomFileType() {
        FileType[] fileTypes = FileType.values();
        return fileTypes[ThreadLocalRandom.current().nextInt(fileTypes.length)];
    }

}
