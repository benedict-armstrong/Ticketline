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
    public void givenNothing_whenSaveUser_thenFindListWithOneElementAndFindUserById() {

        userRepository.save(user);

        assertAll(
            () -> assertEquals(1, userRepository.findAll().size()),
            () -> assertNotNull(userRepository.findById(user.getId()))
        );
    }


}
