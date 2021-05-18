package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public interface TestDataFile extends TestData {

    String FILE_BASE_URI = BASE_URI + "/files";

    byte[] TEST_FILE_DATA = new byte[]{0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa};
    File.Type TEST_FILE_TYPE = File.Type.IMAGE_JPEG;

    File IMAGE_FILE = File.builder()
        .data(TEST_FILE_DATA)
        .type(File.Type.IMAGE_JPEG)
        .build();

    MockMultipartFile MOCK_FILE = new MockMultipartFile(
        "file",
        "file.jpeg",
        MediaType.IMAGE_JPEG_VALUE,
        TEST_FILE_DATA
    );

    MockMultipartFile UNSUPPORTED_MOCK_FILE = new MockMultipartFile(
        "file",
        "file.zz",
        "hello/world",
        TEST_FILE_DATA
    );

    MockMultipartFile NOTYPE_MOCK_FILE = new MockMultipartFile(
        "file",
        "file.zz",
        null,
        TEST_FILE_DATA
    );

}
