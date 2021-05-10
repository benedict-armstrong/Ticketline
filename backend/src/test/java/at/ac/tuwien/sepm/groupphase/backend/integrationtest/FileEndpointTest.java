package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataFile;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FileDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FileEndpointTest implements TestDataFile {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final MockMultipartFile mockMultipartFile = new MockMultipartFile(
        "file",
        "file.jpeg",
        MediaType.IMAGE_JPEG_VALUE,
        TEST_FILE_DATA
    );
    private final MockMultipartFile mockUnsupportedMultipartFile = new MockMultipartFile(
        "file",
        "file.zz",
        "hello/world",
        TEST_FILE_DATA
    );

    @BeforeEach
    public void beforeEach() {
        fileRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return file DTO without data after successful upload")
    public void whenUploadFile_successful_thenGetFileDtoWithoutDataBack() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            multipart(FILE_BASE_URI)
                .file(mockMultipartFile)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        FileDto fileDto = objectMapper.readValue(response.getContentAsString(), FileDto.class);
        assertAll(
            () -> assertNotNull(fileDto.getId()),
            () -> assertNull(fileDto.getData()),
            () -> assertNotNull(fileDto.getType())
        );
    }

    @Test
    @DisplayName("Should return 422 after attempting to upload unsupported file")
    public void whenUploadFile_unsupported_then422() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            multipart(FILE_BASE_URI)
                .file(mockUnsupportedMultipartFile)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 400 when no files are attached")
    public void whenUploadFile_noFileAttached_then400() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            multipart(FILE_BASE_URI)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

}
