package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    private ApplicationUser user = ApplicationUser.UserBuilder.anUser()
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
        user = ApplicationUser.UserBuilder.anUser()
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
    public void whenPostUser_thenUserWithId() throws Exception {

        UserDto userDto = userMapper.applicationUserToUserDto(user);
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

    }

}
