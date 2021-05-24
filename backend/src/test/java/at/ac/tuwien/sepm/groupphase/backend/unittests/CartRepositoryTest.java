package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataCart;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.repository.CartRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CartRepositoryTest implements TestDataCart {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    private CartItem cartItem;

    @BeforeEach
    public void beforeEach() {

        ApplicationUser user = userRepository.save(CART_USER);

        cartItem = CartItem.builder()
            .user(user)
            .ticketId(CART_TICKET_ID)
            .amount(CART_AMOUNT)
            .creationDate(CART_CREATION_DATE)
            .build();
    }

    @AfterEach
    public void afterEach() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void givenNothing_whenSaveCart_thenFindListWithOneElementAndFindUserById() {

        cartRepository.save(cartItem);

        assertAll(
            () -> assertEquals(1, cartRepository.findAll().size()),
            () -> assertNotNull(cartRepository.findById(cartItem.getId()))
        );
    }
}
