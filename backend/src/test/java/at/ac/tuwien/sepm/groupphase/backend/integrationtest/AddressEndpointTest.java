package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataAddress;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
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
public class AddressEndpointTest implements TestAuthentification, TestDataAddress {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    private String authToken;

    private Address address;

    @BeforeEach
    public void beforeEach() throws Exception {
        address = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .build();

        saveUser(AUTH_USER_ORGANIZER, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ORGANIZER, mockMvc, objectMapper);
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 201 and address object with set ID")
    public void whenCreateAddress_then201AndAddressWithId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(ADDRESS_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        AddressDto savedDto = objectMapper.readValue(response.getContentAsString(), AddressDto.class);
        assertAll(
            () -> assertNotNull(savedDto.getId())
        );
    }

    @Test
    @DisplayName("Should return 200 and address with name from search")
    public void whenAddressGiven_SearchWithName_ShouldReturn200AndAddress() throws Exception {
        Address searchAddress = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .eventLocation(true)
            .build();

        addressRepository.save(searchAddress);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ADDRESS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("name", TEST_ADDRESS_NAME)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AddressDto> addressDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), AddressDto[].class)
        );

        assertEquals(searchAddress.getName(), addressDtos.get(0).getName());
        assertEquals(1, addressDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and address with line one from search")
    public void whenAddressGiven_SearchWithLineOne_ShouldReturn200AndAddress() throws Exception {
        Address searchAddress = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .eventLocation(true)
            .build();

        addressRepository.save(searchAddress);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ADDRESS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("lineOne", TEST_ADDRESS_LINE_ONE)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AddressDto> addressDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), AddressDto[].class)
        );

        assertEquals(searchAddress.getLineOne(), addressDtos.get(0).getLineOne());
        assertEquals(1, addressDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and address with line two from search")
    public void whenAddressGiven_SearchWithLineTwo_ShouldReturn200AndAddress() throws Exception {
        Address searchAddress = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .lineTwo(TEST_ADDRESS_LINE_TWO)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .eventLocation(true)
            .build();

        addressRepository.save(searchAddress);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ADDRESS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("lineTwo", TEST_ADDRESS_LINE_TWO)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AddressDto> addressDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), AddressDto[].class)
        );

        assertEquals(searchAddress.getLineTwo(), addressDtos.get(0).getLineTwo());
        assertEquals(1, addressDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and address with city from search")
    public void whenAddressGiven_SearchWithCity_ShouldReturn200AndAddress() throws Exception {
        Address searchAddress = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .eventLocation(true)
            .build();

        addressRepository.save(searchAddress);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ADDRESS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("city", TEST_ADDRESS_CITY)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AddressDto> addressDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), AddressDto[].class)
        );

        assertEquals(searchAddress.getCity(), addressDtos.get(0).getCity());
        assertEquals(1, addressDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and address with postcode from search")
    public void whenAddressGiven_SearchWithPostcode_ShouldReturn200AndAddress() throws Exception {
        Address searchAddress = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .eventLocation(true)
            .build();

        addressRepository.save(searchAddress);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ADDRESS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("postCode", TEST_ADDRESS_POSTCODE)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AddressDto> addressDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), AddressDto[].class)
        );

        assertEquals(searchAddress.getPostcode(), addressDtos.get(0).getPostcode());
        assertEquals(1, addressDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and address with country from search")
    public void whenAddressGiven_SearchWithCountry_ShouldReturn200AndAddress() throws Exception {
        Address searchAddress = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .eventLocation(true)
            .build();

        addressRepository.save(searchAddress);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ADDRESS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("country", TEST_ADDRESS_COUNTRY)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AddressDto> addressDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), AddressDto[].class)
        );

        assertEquals(searchAddress.getCountry(), addressDtos.get(0).getCountry());
        assertEquals(1, addressDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and address with all search params")
    public void whenAddressGiven_SearchWithAllParams_ShouldReturn200AndAddress() throws Exception {
        Address searchAddress = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .lineTwo(TEST_ADDRESS_LINE_TWO)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .eventLocation(true)
            .build();

        addressRepository.save(searchAddress);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ADDRESS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("name", TEST_ADDRESS_NAME)
                .param("lineOne", TEST_ADDRESS_LINE_ONE)
                .param("lineTwo", TEST_ADDRESS_LINE_TWO)
                .param("city", TEST_ADDRESS_CITY)
                .param("postCode", TEST_ADDRESS_POSTCODE)
                .param("country", TEST_ADDRESS_COUNTRY)
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AddressDto> addressDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), AddressDto[].class)
        );

        assertEquals(searchAddress.getName(), addressDtos.get(0).getName());
        assertEquals(searchAddress.getLineOne(), addressDtos.get(0).getLineOne());
        assertEquals(searchAddress.getLineTwo(), addressDtos.get(0).getLineTwo());
        assertEquals(searchAddress.getCity(), addressDtos.get(0).getCity());
        assertEquals(searchAddress.getPostcode(), addressDtos.get(0).getPostcode());
        assertEquals(searchAddress.getCountry(), addressDtos.get(0).getCountry());
        assertEquals(1, addressDtos.size());
    }

    @Test
    @DisplayName("Should return 200 and address with country from search")
    public void whenAddressGiven_SearchWithWrongName_ShouldReturnNothing() throws Exception {
        Address searchAddress = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .eventLocation(true)
            .build();

        addressRepository.save(searchAddress);

        MvcResult mvcResult = this.mockMvc.perform(
            get(ADDRESS_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .param("name", "Wrong name")
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AddressDto> addressDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), AddressDto[].class)
        );

        assertEquals(0, addressDtos.size());
    }
}
