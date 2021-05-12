package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataFile;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class FileRepositoryTest implements TestDataFile {

    @Autowired
    private FileRepository fileRepository;

    private final File file = File.FileBuilder.aFile()
        .withData(TEST_FILE_DATA)
        .withType(TEST_FILE_TYPE)
        .build();

    @BeforeEach
    public void beforeEach(){
        fileRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return correct file entity after saving")
    public void givenNothing_whenSaveFile_thenReturnCorrectEntity() {
        File savedFile = fileRepository.save(file);
        assertAll(
            () -> assertNotNull(savedFile.getId()),
            () -> assertEquals(file.getData(), savedFile.getData()),
            () -> assertEquals(file.getType(), savedFile.getType()),
            () -> assertEquals(savedFile, fileRepository.getOne(savedFile.getId()))
        );
    }

}
