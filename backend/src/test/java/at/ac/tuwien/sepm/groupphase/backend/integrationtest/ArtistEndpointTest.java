package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ArtistEndpointTest implements TestAuthentification, TestDataArtist {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    private String authToken;

    private Artist artist;

    @BeforeEach
    public void beforeEach() throws Exception {
        artist = Artist.builder()
            .firstName(TEST_ARTIST_FIRSTNAME)
            .lastName(TEST_ARTIST_LASTNAME)
            .build();

        saveUser(AUTH_USER_ORGANIZER, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ORGANIZER, mockMvc, objectMapper);
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 201 and artist object with set ID")
    public void whenCreateArtist_then201AndArtistWithId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(ARTIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(artist))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        ArtistDto savedDto = objectMapper.readValue(response.getContentAsString(), ArtistDto.class);
        assertAll(
            () -> assertNotNull(savedDto.getId())
        );
    }

    @Test
    @DisplayName("Should return 400 when no first name is given")
    public void whenCreateArtist_withoutFirstName_then400() throws Exception {
        Artist invalidArtist = Artist.builder().lastName(TEST_ARTIST_LASTNAME).build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(ARTIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidArtist))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return 200 and all artists when no search params are given")
    public void whenArtistGiven_SearchWithNoParams_ShouldReturn200AndArtists() throws Exception {
        Artist searchArtist = Artist.builder().firstName(TEST_ARTIST_FIRSTNAME).lastName(TEST_ARTIST_LASTNAME).build();

        artistRepository.save(searchArtist);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ARTIST_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtistDto> artistDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), ArtistDto[].class)
        );

        assertEquals(1, artistDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and artist with first name from search")
    public void whenArtistGiven_SearchWithFirstName_ShouldReturn200AndArtist() throws Exception {
        Artist searchArtist = Artist.builder().firstName(TEST_ARTIST_FIRSTNAME).lastName(TEST_ARTIST_LASTNAME).build();

        artistRepository.save(searchArtist);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ARTIST_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("firstName", TEST_ARTIST_FIRSTNAME)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtistDto> artistDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), ArtistDto[].class)
        );

        assertEquals(searchArtist.getFirstName(), artistDtos.get(0).getFirstName());
        assertEquals(1, artistDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and artist with last name from search")
    public void whenArtistGiven_SearchWithLastName_ShouldReturn200AndArtist() throws Exception {
        Artist searchArtist = Artist.builder().firstName(TEST_ARTIST_FIRSTNAME).lastName(TEST_ARTIST_LASTNAME).build();

        artistRepository.save(searchArtist);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ARTIST_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("lastName", TEST_ARTIST_LASTNAME)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtistDto> artistDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), ArtistDto[].class)
        );

        assertEquals(searchArtist.getLastName(), artistDtos.get(0).getLastName());
        assertEquals(1, artistDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and artist with all search params")
    public void whenArtistGiven_SearchWithAllParams_ShouldReturn200AndArtist() throws Exception {
        Artist searchArtist = Artist.builder().firstName(TEST_ARTIST_FIRSTNAME).lastName(TEST_ARTIST_LASTNAME).build();

        artistRepository.save(searchArtist);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ARTIST_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("firstName", TEST_ARTIST_FIRSTNAME)
                .param("lastName", TEST_ARTIST_LASTNAME)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtistDto> artistDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), ArtistDto[].class)
        );

        assertEquals(searchArtist.getFirstName(), artistDtos.get(0).getFirstName());
        assertEquals(searchArtist.getLastName(), artistDtos.get(0).getLastName());
        assertEquals(1, artistDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and artist with all search params")
    public void whenArtistGiven_SearchWithWrongFirstName_ShouldReturnNothing() throws Exception {
        Artist searchArtist = Artist.builder().firstName(TEST_ARTIST_FIRSTNAME).lastName(TEST_ARTIST_LASTNAME).build();

        artistRepository.save(searchArtist);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ARTIST_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("firstName", TEST_ARTIST_LASTNAME)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtistDto> artistDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), ArtistDto[].class)
        );

        assertEquals(0, artistDtos.size());
    }
}
