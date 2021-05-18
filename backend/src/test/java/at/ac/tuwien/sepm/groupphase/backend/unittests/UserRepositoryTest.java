package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest implements TestDataUser {

    @Autowired
    private UserRepository userRepository;

    private ApplicationUser user = ApplicationUser.builder()
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
        userRepository.deleteAll();
        user = ApplicationUser.builder()
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

    @Test
    public void givenNothing_whenSaveUser_thenFindListWithOneElementAndFindUserById() {

        userRepository.save(user);

        assertAll(
            () -> assertEquals(1, userRepository.findAll().size()),
            () -> assertNotNull(userRepository.findById(user.getId()))
        );
    }


}
