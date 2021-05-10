package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.FileType;

public interface TestDataFile extends TestData {

    byte[] TEST_FILE_DATA = new byte[]{0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa};
    FileType TEST_FILE_TYPE = FileType.IMAGE_JPEG;

    String FILE_BASE_URI = BASE_URI + "/files";

}
