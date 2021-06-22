package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;

@Profile("generateData")
@Component
public class FileDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final FileRepository fileRepository;

    private static final int NUMBER_OF_FILES_TO_GENERATE = 5 + (2 * EventDataGenerator.getNumberOfEventsToGenerate())
        + (2 * NewsDataGenerator.getNumberOfNewsToGenerate());

    // Free images from pixabay.com
    private static final String[] imageUrls = new String[]{
        "https://cdn.pixabay.com/photo/2013/07/12/17/47/test-pattern-152459_960_720.png",
        "https://cdn.pixabay.com/photo/2015/11/22/19/04/crowd-1056764_960_720.jpg",
        "https://cdn.pixabay.com/photo/2016/11/22/19/15/hand-1850120_960_720.jpg"
    };

    public FileDataGenerator(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostConstruct
    private void generateFiles() throws Exception {
        if (fileRepository.findAll().size() > 0) {
            LOGGER.debug("Files have already been generated");
        } else {
            LOGGER.debug("Generating {} files", imageUrls.length);
            for (int i = 0; i < NUMBER_OF_FILES_TO_GENERATE; i++) {
                File file = File.builder()
                    .data(download(imageUrls[i % imageUrls.length]))
                    .type(getTypeFrom(imageUrls[i % imageUrls.length]))
                    .build();
                LOGGER.debug("Saving file {}", file);
                fileRepository.save(file);
            }
        }
    }

    private byte[] download(String urlText) throws Exception {
        LOGGER.trace("download({})", urlText);
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int n;
            byte [] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
        return output.toByteArray();
    }

    private File.Type getTypeFrom(String url) {
        LOGGER.trace("getTypeFrom({})", url);
        int extensionDotIndex = url.length() - 1;
        for (int i = extensionDotIndex; i >= 0; i--) {
            if (url.charAt(i) == '.') {
                extensionDotIndex = i + 1;
                break;
            }
        }
        String extension = url.substring(extensionDotIndex);
        switch (extension) {
            case "jpg":
                return File.Type.IMAGE_JPG;
            case "png":
                return File.Type.IMAGE_PNG;
            default:
                throw new IllegalArgumentException("Couldn't map extension \"" + extension + "\" to file type for downloading");
        }
    }

}
