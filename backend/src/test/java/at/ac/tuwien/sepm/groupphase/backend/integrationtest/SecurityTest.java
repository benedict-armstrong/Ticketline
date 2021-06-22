package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.BackendApplication;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestAuthentification;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataVenue;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Security is a cross-cutting concern, however for the sake of simplicity it is tested against the event and user endpoint
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityTest implements TestData, TestDataEvent, TestAuthentification, TestDataUser {

    private static final List<Class<?>> mappingAnnotations = Lists.list(
        RequestMapping.class,
        GetMapping.class,
        PostMapping.class,
        PutMapping.class,
        PatchMapping.class,
        DeleteMapping.class
    );

    private static final List<Class<?>> securityAnnotations = Lists.list(
        Secured.class,
        PreAuthorize.class,
        RolesAllowed.class,
        PermitAll.class,
        DenyAll.class,
        DeclareRoles.class
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;

    private EventDto eventDto;

    private ArtistDto artistDto;

    private VenueDto venueDto;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private List<Object> components;

    @BeforeEach
    public void beforeEach() throws Exception {
        artistDto = TestDataArtist.getArtistDto();
        venueDto = TestDataVenue.getVenueDto();

        PerformanceDto[] performanceDtos = new PerformanceDto[1];
        performanceDtos[0] = PerformanceDto.builder()
            .title(TestDataEvent.TEST_EVENT_PERFORMANCE_TITLE)
            .description(TestDataEvent.TEST_EVENT_PERFORMANCE_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE)
            .ticketTypes(TestDataTicket.getTicketTypeDtos())
            .build();

        eventDto = EventDto.builder()
            .name(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .startDate(TestDataEvent.TEST_EVENT_DATE_FUTURE)
            .endDate(TestDataEvent.TEST_EVENT_DATE_FUTURE2)
            .duration(TestDataEvent.TEST_EVENT_DURATION)
            .eventType(TestDataEvent.TEST_EVENT_EVENT_TYPE)
            .performances(performanceDtos)
            .build();

        saveUser(AUTH_USER_ADMIN, userRepository, passwordEncoder);
        authToken = authenticate(AUTH_USER_ADMIN, mockMvc, objectMapper);
    }

    @AfterEach
    public void afterEach() {
        eventRepository.deleteAll();
        performanceRepository.deleteAll();
        venueRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    private ArtistDto saveArtist(ArtistDto artistDto) throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(ARTIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(artistDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        return objectMapper.readValue(response.getContentAsString(),
            ArtistDto.class);
    }

    private VenueDto saveVenue(VenueDto venueDto) throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            post(VENUE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        return objectMapper.readValue(response.getContentAsString(),
            VenueDto.class);
    }

    /**
     * This ensures every Rest Method is secured with Method Security.
     * It is very easy to forget securing one method causing a security vulnerability.
     * Feel free to remove / disable / adapt if you do not use Method Security (e.g. if you prefer Web Security to define who may perform which actions) or want to use Method Security on the service layer.
     */
    @Test
    public void ensureSecurityAnnotationPresentForEveryEndpoint() throws Exception {
        List<Pair<Class<?>, Method>> notSecured = components.stream()
            .map(AopUtils::getTargetClass) // beans may be proxies, get the target class instead
            .filter(clazz -> clazz.getCanonicalName() != null && clazz.getCanonicalName().startsWith(BackendApplication.class.getPackageName())) // limit to our package
            .filter(clazz -> clazz.getAnnotation(RestController.class) != null) // limit to classes annotated with @RestController
            .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()).map(method -> new ImmutablePair<Class<?>, Method>(clazz, method))) // get all class -> method pairs
            .filter(pair -> Arrays.stream(pair.getRight().getAnnotations()).anyMatch(annotation -> mappingAnnotations.contains(annotation.annotationType()))) // keep only the pairs where the method has a "mapping annotation"
            .filter(pair -> Arrays.stream(pair.getRight().getAnnotations()).noneMatch(annotation -> securityAnnotations.contains(annotation.annotationType()))) // keep only the pairs where the method does not have a "security annotation"
            .collect(Collectors.toList());

        assertThat(notSecured.size())
            .as("Most rest methods should be secured. If one is really intended for public use, explicitly state that with @PermitAll. "
                + "The following are missing: \n" + notSecured.stream().map(pair -> "Class: " + pair.getLeft() + " Method: " + pair.getRight()).reduce("", (a, b) -> a + "\n" + b))
            .isZero();

    }

    @Test
    public void givenUserLoggedIn_whenFindAll_then200() throws Exception {
        ApplicationUser user = ApplicationUser.builder()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL + "e")
            .lastLogin(DEFAULT_LAST_LOGIN)
            .role(ApplicationUser.UserRole.ADMIN)
            .status(DEFAULT_STATUS)
            .password(DEFAULT_PASSWORD)
            .points(DEFAULT_POINTS)
            .telephoneNumber(DEFAULT_PHONE_NUMBER)
            .build();

        saveUser(user, userRepository, passwordEncoder);
        String userAuthToken = authenticate(user, mockMvc, objectMapper);

        MvcResult mvcResult = this.mockMvc.perform(
            get(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
                .header(securityProperties.getAuthHeader(), userAuthToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void givenAdminLoggedIn_whenPost_then201() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void givenNoOneLoggedIn_whenPost_then403() throws Exception {
        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void givenUserLoggedIn_whenPost_then403() throws Exception {
        saveUser(AUTH_USER_CLIENT, userRepository, passwordEncoder);
        String userAuthToken = authenticate(AUTH_USER_CLIENT, mockMvc, objectMapper);

        eventDto.getPerformances()[0].setArtist(saveArtist(artistDto));
        eventDto.getPerformances()[0].setVenue(saveVenue(venueDto));

        MvcResult mvcResult = this.mockMvc.perform(
            post(TestDataEvent.EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                .header(securityProperties.getAuthHeader(), userAuthToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void givenNoOneLoggedIn_SettingUserRole_then401() throws Exception {
        ApplicationUser user = ApplicationUser.builder()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .lastLogin(DEFAULT_LAST_LOGIN)
            .address(DEFAULT_ADDRESS)
            .role(ApplicationUser.UserRole.ADMIN)
            .status(DEFAULT_STATUS)
            .password(DEFAULT_PASSWORD)
            .points(DEFAULT_POINTS)
            .telephoneNumber(DEFAULT_PHONE_NUMBER)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(USER_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void givenAdminLoggedIn_SettingUserRole_then201() throws Exception {
        ApplicationUser user = ApplicationUser.builder()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL + "e")
            .lastLogin(DEFAULT_LAST_LOGIN)
            .address(
                Address.builder().name("Max Mustermann").lineOne("Teststraße 1").city("Wien").postcode("1010").country("Österreich").build()
            )
            .role(ApplicationUser.UserRole.ADMIN)
            .status(DEFAULT_STATUS)
            .password(DEFAULT_PASSWORD)
            .points(DEFAULT_POINTS)
            .telephoneNumber(DEFAULT_PHONE_NUMBER)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(USER_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .header(securityProperties.getAuthHeader(), authToken)
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void logInWithWrongPassword_then401() throws Exception {
        ApplicationUser user = ApplicationUser.builder()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL + "e")
            .lastLogin(DEFAULT_LAST_LOGIN)
            .role(ApplicationUser.UserRole.ADMIN)
            .status(DEFAULT_STATUS)
            .password(DEFAULT_PASSWORD)
            .points(DEFAULT_POINTS)
            .telephoneNumber(DEFAULT_PHONE_NUMBER)
            .build();

        saveUser(user, userRepository, passwordEncoder);

        UserLoginDto userLoginDto = UserLoginDto.builder().email(user.getEmail()).password("Wrong").build();

        MvcResult mvcResult = this.mockMvc.perform(
            post(LOGIN_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDto))
        ).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }
}
