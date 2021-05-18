package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataCart;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.repository.CartRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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
    private EventRepository eventRepository;

    @Autowired
    private CartRepository cartRepository;

    private List<CartItem> cart = new LinkedList<CartItem>();

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        eventRepository.deleteAll();
        cartRepository.deleteAll();

        userRepository.save(CART_USER);
        eventRepository.save(CART_EVENT);

        cart.add(CartItem.CartItemBuilder.aCartItem()
            .withUser(CART_USER)
            .withEvent(CART_EVENT)
            .withSectorType(CART_SECTOR_TYPE)
            .withAmount(CART_AMOUNT)
            .build()
        );
    }

    @Test
    public void givenNothing_whenSaveCart_thenFindListWithOneElementAndFindUserById() {

        cartRepository.saveAll(cart);

        assertAll(
            () -> assertEquals(1, cartRepository.findAll().size()),
            () -> assertNotNull(cartRepository.findById(cart.get(0).getId()))
        );
    }
}
