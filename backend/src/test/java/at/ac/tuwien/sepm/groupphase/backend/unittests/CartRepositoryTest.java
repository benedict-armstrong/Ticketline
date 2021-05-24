package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataCart;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.repository.CartRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    private CartItem cartItem, cartItem2, cartItem3;

    private ApplicationUser user;
    private ApplicationUser user2;

    @BeforeEach
    public void beforeEach() {

        user = userRepository.save(CART_USER);
        user2 = userRepository.save(CART_USER2);

        cartItem = CartItem.builder()
            .user(user)
            .ticketId(CART_TICKET_ID)
            .amount(CART_AMOUNT)
            .creationDate(CART_CREATION_DATE)
            .build();

        cartItem2 = CartItem.builder()
            .user(user)
            .ticketId(CART_TICKET_ID)
            .amount(CART_AMOUNT)
            .creationDate(CART_CREATION_DATE)
            .build();

        cartItem3 = CartItem.builder()
            .user(user2)
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
    public void whenAddingCartItem_thenGetCartItemWithId() {

        cartRepository.save(cartItem);

        assertAll(
            () -> assertEquals(1, cartRepository.findAll().size()),
            () -> assertNotNull(cartRepository.findById(cartItem.getId()))
        );
    }

    @Test
    public void givenTwoCartItems_whenGettingCart_thenGetListOfCartItemsWithLengthOfTwo() {
        cartRepository.save(cartItem);
        cartRepository.save(cartItem2);
        cartRepository.save(cartItem3);

        List<CartItem> response = cartRepository.findByUser(user);

        assertAll(
            () -> assertEquals(2, response.size()),
            () -> assertNotNull(response.get(0)),
            () -> assertNotNull(response.get(1))
        );
    }
}
