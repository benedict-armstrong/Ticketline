package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public interface TestAuthentification extends TestDataUser {

    String AUTH_BASE_URI = BASE_URI + "/authentication";

    ApplicationUser AUTH_USER_ADMIN = ApplicationUser.UserBuilder.anUser()
        .withFirstName(DEFAULT_FIRST_NAME)
        .withLastName(DEFAULT_LAST_NAME)
        .withEmail(DEFAULT_EMAIL)
        .withLastLogin(DEFAULT_LAST_LOGIN)
        .withRole(ApplicationUser.UserRole.ADMIN)
        .withStatus(DEFAULT_STATUS)
        .withPassword(DEFAULT_PASSWORD)
        .withPoints(DEFAULT_POINTS)
        .withTelephoneNumber(DEFAULT_PHONE_NUMBER)
        .build();

    ApplicationUser AUTH_USER_ORGANIZER = ApplicationUser.UserBuilder.anUser()
        .withFirstName(DEFAULT_FIRST_NAME)
        .withLastName(DEFAULT_LAST_NAME)
        .withEmail(DEFAULT_EMAIL)
        .withLastLogin(DEFAULT_LAST_LOGIN)
        .withRole(ApplicationUser.UserRole.ORGANIZER)
        .withStatus(DEFAULT_STATUS)
        .withPassword(DEFAULT_PASSWORD)
        .withPoints(DEFAULT_POINTS)
        .withTelephoneNumber(DEFAULT_PHONE_NUMBER)
        .build();

    ApplicationUser AUTH_USER_CLIENT = ApplicationUser.UserBuilder.anUser()
        .withFirstName(DEFAULT_FIRST_NAME)
        .withLastName(DEFAULT_LAST_NAME)
        .withEmail(DEFAULT_EMAIL)
        .withLastLogin(DEFAULT_LAST_LOGIN)
        .withRole(ApplicationUser.UserRole.CLIENT)
        .withStatus(DEFAULT_STATUS)
        .withPassword(DEFAULT_PASSWORD)
        .withPoints(DEFAULT_POINTS)
        .withTelephoneNumber(DEFAULT_PHONE_NUMBER)
        .build();

    /**
     * Mocks authentication for usage in tests.
     *
     * @param user the user that is authenticating - should be present in the database.
     * @param mockMvc a MockMvc object.
     * @param objectMapper an ObjectMapper object.
     * @return the token that a server normally sends back.
     * @throws Exception if something went wrong during mocking the request.
     */
    default String authenticate(ApplicationUser user, MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
            post(AUTH_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    objectMapper.writeValueAsString(new AuthDto(user.getEmail(), user.getPassword()))
                )
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        return response.getContentAsString();
    }

    /**
     * Saves a user in the database for usage in tests.
     *
     * @param user the user to be saved.
     * @param userRepository the repository where the user is to be saved.
     * @param passwordEncoder a password encoder object.
     * @return the user entity that has been saved.
     */
    default ApplicationUser saveUser(ApplicationUser user,
                                     UserRepository userRepository,
                                     PasswordEncoder passwordEncoder) {
        ApplicationUser userWithEncodedPassword = ApplicationUser.UserBuilder.anUser()
            .withFirstName(user.getFirstName())
            .withLastName(user.getLastName())
            .withEmail(user.getEmail())
            .withLastLogin(user.getLastLogin())
            .withRole(user.getRole())
            .withStatus(user.getStatus())
            .withPassword(passwordEncoder.encode(user.getPassword()))
            .withPoints(user.getPoints())
            .withAddress(user.getAddress())
            .withTelephoneNumber(user.getTelephoneNumber())
            .build();
        return userRepository.save(userWithEncodedPassword);
    }

}

class AuthDto {

    private final String email;
    private final String password;

    public AuthDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}