package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserEndpointTest implements TestDataUser {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    private ApplicationUser defaultUser = ApplicationUser.UserBuilder.anUser()
        .withFirstName(DEFAULT_FIRST_NAME)
        .withLastName(DEFAULT_LAST_NAME)
        .withEmail(DEFAULT_EMAIL)
        .withLastLogin(DEFAULT_LAST_LOGIN)
        .withRole(DEFAULT_ROLE)
        .withStatus(DEFAULT_STATUS)
        .withPassword(DEFAULT_PASSWORD)
        .withPoints(DEFAULT_POINTS)
        .withAddress(DEFAULT_ADDRESS)
        .withTelephoneNumber(DEFAULT_PHONE_NUMBER)
        .build();

    private ApplicationUser adminUser = ApplicationUser.UserBuilder.anUser()
        .withFirstName(ADMIN_FIRST_NAME)
        .withLastName(ADMIN_LAST_NAME)
        .withEmail(ADMIN_EMAIL)
        .withLastLogin(ADMIN_LAST_LOGIN)
        .withRole(ADMIN_ROLE)
        .withStatus(ADMIN_USER_STATUS)
        .withPassword(ADMIN_PASSWORD)
        .withPoints(ADMIN_POINTS)
        .withAddress(ADMIN_ADDRESS)
        .withTelephoneNumber(ADMIN_PHONE_NUMBER)
        .build();

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        defaultUser = ApplicationUser.UserBuilder.anUser()
            .withFirstName(DEFAULT_FIRST_NAME)
            .withLastName(DEFAULT_LAST_NAME)
            .withEmail(DEFAULT_EMAIL)
            .withLastLogin(DEFAULT_LAST_LOGIN)
            .withRole(DEFAULT_ROLE)
            .withStatus(DEFAULT_STATUS)
            .withPassword(DEFAULT_PASSWORD)
            .withPoints(DEFAULT_POINTS)
            .withAddress(DEFAULT_ADDRESS)
            .withTelephoneNumber(DEFAULT_PHONE_NUMBER)
            .build();

        adminUser = ApplicationUser.UserBuilder.anUser()
            .withFirstName(ADMIN_FIRST_NAME)
            .withLastName(ADMIN_LAST_NAME)
            .withEmail(ADMIN_EMAIL)
            .withLastLogin(ADMIN_LAST_LOGIN)
            .withRole(ADMIN_ROLE)
            .withStatus(ADMIN_USER_STATUS)
            .withPassword(ADMIN_PASSWORD)
            .withPoints(ADMIN_POINTS)
            .withAddress(ADMIN_ADDRESS)
            .withTelephoneNumber(ADMIN_PHONE_NUMBER)
            .build();
    }

    @Test
    public void whenPostUser_thenUserWithIdAndLastLogin() throws Exception {

        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        UserDto userResponse = objectMapper.readValue(response.getContentAsString(),
            UserDto.class);

        assertNotNull(userResponse.getId());
        assertNotNull(userResponse.getLastLogin());

    }

    @Test
    public void whenPostUsersWithSameEmail_then422() throws Exception {

        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        UserDto userResponse = objectMapper.readValue(response.getContentAsString(),
            UserDto.class);

        assertNotNull(userResponse.getId());

        MvcResult mvcResult2 = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response2 = mvcResult2.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response2.getStatus());
    }

    @Test
    public void whenPostUserWithNoName_then400() throws Exception {

        defaultUser.setFirstName(null);

        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    public void whenPostUserWithBadEmail_then400() throws Exception {

        defaultUser.setEmail("bad.email");

        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    public void whenPostUserAsAdminSetUserRoleAdmin_then200_UserIsAdmin() throws Exception {

        defaultUser.setRole(ApplicationUser.UserRole.ADMIN);

        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        UserDto userResponse = objectMapper.readValue(response.getContentAsString(),
            UserDto.class);

        assertNotNull(userResponse.getId());
        assertEquals(userResponse.getRole(), ApplicationUser.UserRole.ADMIN);

    }

    @Test
    public void whenPostUserAsUserSetUserRoleAdmin_then401() throws Exception {

        defaultUser.setRole(ApplicationUser.UserRole.ADMIN);

        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());

    }

    @Test
    public void whenPostUserAsUserNotSetUserRole_then200_UserRoleClient() throws Exception {

        defaultUser.setRole(null);

        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        UserDto userResponse = objectMapper.readValue(response.getContentAsString(),
            UserDto.class);

        assertNotNull(userResponse.getId());
        assertEquals(userResponse.getRole(), ApplicationUser.UserRole.CLIENT);

    }

}
