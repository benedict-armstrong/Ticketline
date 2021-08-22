package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataVenue;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LayoutUnitDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;

import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class VenueEndpointTest implements TestDataVenue, TestAuthentification {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    private String authToken;

    private VenueDto venueDto = VenueDto.builder()
        .name(VENUE_NAME)
        .sectors( Arrays.asList(SECTOR_DTO_SEATED, SECTOR_DTO_STAGE, SECTOR_DTO_STANDING))
        .address(
            AddressDto.builder()
                .name(VENUE_ADDRESS_NAME)
                .lineOne(VENUE_ADDRESS_LINE_ONE)
                .city(VENUE_ADDRESS_CITY)
                .postcode(VENUE_ADDRESS_POSTCODE)
                .country(VENUE_ADDRESS_COUNTRY)
                .eventLocation(false)
                .build())
        .layout(VENUE_LAYOUT_DTO)
        .build();

    @BeforeEach
    public void beforeEach() throws Exception {
        venueDto = VenueDto.builder()
            .name("VenueDto")
            .sectors( Arrays.asList(SECTOR_DTO_SEATED, SECTOR_DTO_STAGE, SECTOR_DTO_STANDING))
            .address(
                AddressDto.builder()
                    .name(VENUE_ADDRESS_NAME)
                    .lineOne(VENUE_ADDRESS_LINE_ONE)
                    .city(VENUE_ADDRESS_CITY)
                    .postcode(VENUE_ADDRESS_POSTCODE)
                    .country(VENUE_ADDRESS_COUNTRY)
                    .eventLocation(false)
                    .build())
            .layout(VENUE_LAYOUT_DTO)
            .build();

        saveUser(AUTH_USER_ORGANIZER, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ORGANIZER, mockMvc, objectMapper);
    }

    @AfterEach
    public void afterEach() {
        venueRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("Should return empty list when getting all venues but there are none")
    public void givenNothing_whenGetAllVenues_thenReturnEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(VENUE_BASE_URI)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<VenueDto> venueDtoList = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            VenueDto[].class));

        assertEquals(0, venueDtoList.size());
    }

    @Test
    @DisplayName("Should return 201 and Venue with an Id when posting a Venue")
    public void postVenue_thenReturn201AndVenueWithId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        VenueDto venueDto = objectMapper.readValue(response.getContentAsString(),
            VenueDto.class);

        assertNotNull(venueDto);
        assertNotNull(venueDto.getId());
    }

    @Test
    @DisplayName("when posting a Venue without layout should return 400")
    public void postVenueWithEmptyLayout_thenReturn400() throws Exception {

        venueDto.setLayout(null);

        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("when posting a Venue with 1 by 1 layout should return 400")
    public void postVenueWith1By1Layout_thenReturn400() throws Exception {

        venueDto.setLayout(
            Collections.singletonList(
                Collections.singletonList(
                    LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("1by1").build()
                )
            )
        );

        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("when posting a Venue with malformed layout should return 400")
    public void postVenueWithMalformedLayout_thenReturn400() throws Exception {

        venueDto.setLayout(
            Arrays.asList(
                Arrays.asList(
                    LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("1").build(),
                    LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("2").build(),
                    LayoutUnitDto.builder().sector(SECTOR_DTO_STAGE).customLabel("3").build()
                ),
                Arrays.asList(
                    LayoutUnitDto.builder().sector(SECTOR_DTO_SEATED).customLabel("4").build(),
                    LayoutUnitDto.builder().sector(SECTOR_DTO_SEATED).customLabel("5").build()
                )
            )
        );

        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("when user inserts venue user can retrieve venue by id")
    public void afterInsertingVenue_OwnerCanRetrieveById() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        VenueDto venueDto = objectMapper.readValue(response.getContentAsString(),
            VenueDto.class);

        MvcResult mvcResult2 = this.mockMvc.perform(
            get(VENUE_BASE_URI + "/" + venueDto.getId())
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response2 = mvcResult2.getResponse();
        assertEquals(HttpStatus.OK.value(), response2.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response2.getContentType());

        VenueDto venueDto2 = objectMapper.readValue(response2.getContentAsString(),
            VenueDto.class);

        assertEquals(venueDto, venueDto2);

    }

    @Test
    @DisplayName("when user inserts venue user can retrieve all (not Empty)")
    public void afterInsertingVenue_OwnerCanRetrieveAll_returnNotEmpty() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        VenueDto venueDto = objectMapper.readValue(response.getContentAsString(),
            VenueDto.class);

        MvcResult mvcResult2 = this.mockMvc.perform(
            get(VENUE_BASE_URI)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response2 = mvcResult2.getResponse();
        assertEquals(HttpStatus.OK.value(), response2.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response2.getContentType());

        List<VenueDto> venueDtoList = Arrays.asList(objectMapper.readValue(response2.getContentAsString(),
            VenueDto[].class));

        assertEquals(1, venueDtoList.size());
        assertEquals(venueDto, venueDtoList.get(0));
    }

}
