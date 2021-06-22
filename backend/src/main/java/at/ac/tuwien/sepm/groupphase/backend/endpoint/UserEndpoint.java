package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(value = "/api/v1/users")
public class UserEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserEndpoint(UserService userService, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping
    @PermitAll
    @Operation(summary = "Add User")
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        LOGGER.info("POST /api/v1/users body: {}", userDto);
        return userMapper.applicationUserToUserDto(userService.addUser(userMapper.userDtoToApplicationUser(userDto)));
    }

    @Transactional
    @GetMapping(value = {"/{email}"})
    @PermitAll
    @Operation(summary = "Find User by Email")
    public UserDto findByEmail(@Valid @PathVariable("email") String email) {
        LOGGER.info("GET /api/v1/users/{}", email);
        return userMapper.applicationUserToUserDto(userService.findApplicationUserByEmail(email));
    }

    @Transactional
    @GetMapping(value = {"/id/{id}"})
    @PermitAll
    @Operation(summary = "Find User by Id")
    public UserDto findById(@Valid @PathVariable("id") long id) {
        LOGGER.info("GET /api/v1/users/{}", id);
        return userMapper.applicationUserToUserDto(userService.findApplicationUserById(id));
    }

    @PutMapping
    @PermitAll
    @Operation(summary = "Update User")
    public UserDto update(@Valid @RequestBody UserDto userDto) {
        LOGGER.info("PUT /api/v1/users body:{}", userDto);
        return userMapper.applicationUserToUserDto(userService.updateUser(userMapper.userDtoToApplicationUser(userDto)));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    @Operation(summary = "Update User")
    public UserDto updateLastRead(@Valid @PathVariable("id") Long userId, @Valid @RequestBody Long lastReadNewsId) {
        LOGGER.info("PUT /api/v1/users/{} (lastReadNews={})", userId, lastReadNewsId);
        return userMapper.applicationUserToUserDto(userService.updateLastRead(userId, lastReadNewsId));
    }

    @PutMapping("/reset")
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reset User password")
    public UserDto resetPassword(@Valid @RequestBody String email) {
        LOGGER.info("PUT /api/v1/users/reset {}", email);
        return userMapper.applicationUserToUserDto(userService.resetPassword(userService.findApplicationUserByEmail(email)));
    }
}
