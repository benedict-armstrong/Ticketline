package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataAddress;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChangePasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.PasswordResetToken;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PasswordTokenRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserEndpointTest implements TestDataUser, TestDataAddress {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    private ApplicationUser defaultUser = ApplicationUser.builder()
        .firstName(DEFAULT_FIRST_NAME)
        .lastName(DEFAULT_LAST_NAME)
        .email(DEFAULT_EMAIL)
        .lastLogin(DEFAULT_LAST_LOGIN)
        .role(DEFAULT_ROLE)
        .status(DEFAULT_STATUS)
        .password(DEFAULT_PASSWORD)
        .points(DEFAULT_POINTS)
        .address(DEFAULT_ADDRESS)
        .telephoneNumber(DEFAULT_PHONE_NUMBER)
        .build();

    private ApplicationUser adminUser = ApplicationUser.builder()
        .firstName(ADMIN_FIRST_NAME)
        .lastName(ADMIN_LAST_NAME)
        .email(ADMIN_EMAIL)
        .lastLogin(ADMIN_LAST_LOGIN)
        .role(ADMIN_ROLE)
        .status(ADMIN_USER_STATUS)
        .password(ADMIN_PASSWORD)
        .points(ADMIN_POINTS)
        .address(ADMIN_ADDRESS)
        .telephoneNumber(ADMIN_PHONE_NUMBER)
        .build();

    @BeforeEach
    public void beforeEach() {
        defaultUser = ApplicationUser.builder()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .lastLogin(DEFAULT_LAST_LOGIN)
            .role(DEFAULT_ROLE)
            .status(DEFAULT_STATUS)
            .password(DEFAULT_PASSWORD)
            .points(DEFAULT_POINTS)
            .address(TestDataAddress.getAddress())
            .telephoneNumber(DEFAULT_PHONE_NUMBER)
            .build();

        adminUser = ApplicationUser.builder()
            .firstName(ADMIN_FIRST_NAME)
            .lastName(ADMIN_LAST_NAME)
            .email(ADMIN_EMAIL)
            .lastLogin(ADMIN_LAST_LOGIN)
            .role(ADMIN_ROLE)
            .status(ADMIN_USER_STATUS)
            .password(ADMIN_PASSWORD)
            .points(ADMIN_POINTS)
            .address(ADMIN_ADDRESS)
            .telephoneNumber(ADMIN_PHONE_NUMBER)
            .build();
    }

    @AfterEach
    public void afterEach() {
        passwordTokenRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
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

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
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

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
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
    public void whenPostUserAsAdminSetUserRoleAdmin_then201_UserIsAdmin() throws Exception {

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

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

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

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        UserDto userResponse = objectMapper.readValue(response.getContentAsString(),
            UserDto.class);

        assertNotNull(userResponse.getId());
        assertEquals(userResponse.getRole(), ApplicationUser.UserRole.CLIENT);

    }

    @Test
    public void whenBannedUserLogin_then403() throws Exception {
        defaultUser.setStatus(ApplicationUser.UserStatus.BANNED);

        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult1 = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response1 = mvcResult1.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response1.getStatus());

        UserLoginDto userLoginDto = UserLoginDto.builder().email(userDto.getEmail()).password(userDto.getPassword()).build();

        MvcResult mvcResult2 = this.mockMvc.perform(post(LOGIN_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userLoginDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response2 = mvcResult2.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatus());
    }

    @Test
    public void whenPasswordWrong3Times_thenUserBanned() throws Exception {
        UserDto userDto = userMapper.applicationUserToUserDto(defaultUser);
        String body = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResultCreate = this.mockMvc.perform(post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse responseCreate = mvcResultCreate.getResponse();

        assertEquals(HttpStatus.CREATED.value(), responseCreate.getStatus());

        UserLoginDto userLoginDto = UserLoginDto.builder().email(userDto.getEmail()).password("Wrong").build();

        for (int i = 0; i < 4; i++) {
            MvcResult mvcResultLogin = this.mockMvc.perform(post(LOGIN_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDto)))
                .andDo(print())
                .andReturn();
            MockHttpServletResponse responseLogin = mvcResultLogin.getResponse();

            assertEquals(HttpStatus.UNAUTHORIZED.value(), responseLogin.getStatus());
        }

        ApplicationUser user = userRepository.findUserByEmail(userDto.getEmail());

        assertEquals(ApplicationUser.UserStatus.BANNED, user.getStatus());
    }

    @Test
    public void whenDeleteSelf_thenRemoveInfoAndReturn204() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.CLIENT);
        ApplicationUser savedUser = userRepository.save(defaultUser);

        MvcResult mvcResult = this.mockMvc.perform(
            delete(USER_BASE_URI + "/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(savedUser.getEmail(), USER_ROLES))
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void whenNonClientDeleteSelf_then409() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.ADMIN);
        ApplicationUser savedUser = userRepository.save(defaultUser);

        MvcResult mvcResult = this.mockMvc.perform(
            delete(USER_BASE_URI + "/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(savedUser.getEmail(), USER_ROLES))
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    public void whenAttemptToDeleteDeletedUser_then404() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.CLIENT);
        ApplicationUser savedUser = userRepository.save(defaultUser);
        String token = jwtTokenizer.getAuthToken(savedUser.getEmail(), USER_ROLES);

        MvcResult mvcResult1 = this.mockMvc.perform(
            delete(USER_BASE_URI + "/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();
        MockHttpServletResponse response1 = mvcResult1.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response1.getStatus());

        MvcResult mvcResult2 = this.mockMvc.perform(
            delete(USER_BASE_URI + "/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();
        MockHttpServletResponse response2 = mvcResult2.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response2.getStatus());
    }

    @Test
    public void whenGetUserByEmail_then200AndUser() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.CLIENT);
        ApplicationUser savedUser = userRepository.save(defaultUser);
        String token = jwtTokenizer.getAuthToken(savedUser.getEmail(), USER_ROLES);

        MvcResult mvcResult = this.mockMvc.perform(
            get(USER_BASE_URI + "/" + savedUser.getEmail())
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        UserDto userResponse = objectMapper.readValue(response.getContentAsString(),
            UserDto.class);

        assertAll(
            () -> assertNotNull(userResponse),
            () -> assertEquals(savedUser.getEmail(), userResponse.getEmail())
        );
    }

    @Test
    public void whenGetUserById_then200AndUser() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.CLIENT);
        ApplicationUser savedUser = userRepository.save(defaultUser);
        String token = jwtTokenizer.getAuthToken(savedUser.getEmail(), USER_ROLES);

        MvcResult mvcResult = this.mockMvc.perform(
            get(USER_BASE_URI + "/id/" + savedUser.getId())
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        UserDto userResponse = objectMapper.readValue(response.getContentAsString(),
            UserDto.class);

        assertAll(
            () -> assertNotNull(userResponse),
            () -> assertEquals(savedUser.getId(), userResponse.getId())
        );
    }

    @Test
    public void whenUpdateUser_then200AndUserWithNewData() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.CLIENT);
        ApplicationUser savedUser = userRepository.save(defaultUser);
        String token = jwtTokenizer.getAuthToken(savedUser.getEmail(), USER_ROLES);

        savedUser.setFirstName("NewName");
        savedUser.setLastName("NewLastName");

        MvcResult mvcResult = this.mockMvc.perform(
            put(USER_BASE_URI)
                .content(
                    objectMapper.writeValueAsString(userMapper.applicationUserToUserDto(savedUser))
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        UserDto userResponse = objectMapper.readValue(response.getContentAsString(),
            UserDto.class);

        assertAll(
            () -> assertNotNull(userResponse),
            () -> assertEquals(savedUser.getFirstName(), userResponse.getFirstName()),
            () -> assertEquals(savedUser.getLastLogin(), userResponse.getLastLogin()),
            () -> assertEquals(savedUser.getId(), userResponse.getId())
        );
    }

    @Test
    public void whenGetAllUsers_then200AndListOfUsers() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.CLIENT);
        ApplicationUser savedUser = userRepository.save(defaultUser);

        MvcResult mvcResult = this.mockMvc.perform(
            get(USER_BASE_URI)
                .param("page", "0")
                .param("size", "10")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        UserDto[] userDtos = objectMapper.readValue(
            response.getContentAsString(), UserDto[].class
        );

        assertAll(
            () -> assertNotNull(userDtos),
            () -> assertEquals(1, userDtos.length),
            () -> assertEquals(savedUser.getId(), userDtos[0].getId())
        );
    }

    @Test
    public void whenUserResetPassword_andLoginWithNewPassword_then200() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.CLIENT);
        ApplicationUser savedUser = userRepository.save(defaultUser);
        String token = jwtTokenizer.getAuthToken(savedUser.getEmail(), USER_ROLES);

        MvcResult mvcResult1 = this.mockMvc.perform(
            post(USER_BASE_URI + "/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(savedUser.getEmail())
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();
        MockHttpServletResponse response1 = mvcResult1.getResponse();
        assertEquals(HttpStatus.OK.value(), response1.getStatus());

        List<PasswordResetToken> passwordResetToken = passwordTokenRepository.findAllByExpiryTimeBefore(LocalDateTime.now().plusDays(2));
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder().password("newPassword").token(passwordResetToken.get(0).getToken()).build();

        MvcResult mvcResult2 = this.mockMvc.perform(
            put(USER_BASE_URI + "/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordDto))
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();

        MockHttpServletResponse response2 = mvcResult2.getResponse();
        assertEquals(HttpStatus.OK.value(), response2.getStatus());

        UserLoginDto userLoginDto = UserLoginDto.builder().email(savedUser.getEmail()).password("newPassword").build();

        MvcResult mvcResult3 = this.mockMvc.perform(
            post(LOGIN_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDto))
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();
        MockHttpServletResponse response3 = mvcResult3.getResponse();
        assertEquals(HttpStatus.OK.value(), response3.getStatus());
    }

    @Test
    public void whenUserResetPassword_tokenShouldBeDeleted() throws Exception {
        defaultUser.setRole(ApplicationUser.UserRole.CLIENT);
        ApplicationUser savedUser = userRepository.save(defaultUser);
        String token = jwtTokenizer.getAuthToken(savedUser.getEmail(), USER_ROLES);

        MvcResult mvcResult1 = this.mockMvc.perform(
            post(USER_BASE_URI + "/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(savedUser.getEmail())
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();
        MockHttpServletResponse response1 = mvcResult1.getResponse();
        assertEquals(HttpStatus.OK.value(), response1.getStatus());

        List<PasswordResetToken> passwordResetToken = passwordTokenRepository.findAllByExpiryTimeBefore(LocalDateTime.now().plusDays(2));
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder().password("newPassword").token(passwordResetToken.get(0).getToken()).build();

        MvcResult mvcResult2 = this.mockMvc.perform(
            put(USER_BASE_URI + "/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordDto))
                .header(securityProperties.getAuthHeader(), token)
        ).andReturn();

        MockHttpServletResponse response2 = mvcResult2.getResponse();
        assertEquals(HttpStatus.OK.value(), response2.getStatus());

        Optional<PasswordResetToken> nonExistentPasswordToken = passwordTokenRepository.findById(passwordResetToken.get(0).getId());
        assertTrue(nonExistentPasswordToken.isEmpty());
    }

}
