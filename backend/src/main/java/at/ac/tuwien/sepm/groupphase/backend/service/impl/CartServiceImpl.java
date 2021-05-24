package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.repository.CartRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class CartServiceImpl implements CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartItem addCart(CartItem cartItem) {
        LOGGER.trace("addCart({})", cartItem);
        cartItem.setCreationDate(LocalDateTime.now());
        return cartRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getCart(Long userId) {
        LOGGER.trace("getCart({})", userId);
        return cartRepository.findByUser(userRepository.getOne(userId));
    }

    @Scheduled(fixedDelay = 60000)
    private void pruneCartItems() {
        LOGGER.trace("pruneCartItems()");
        List<CartItem> toBeDeleted = cartRepository.findByCreationDateBefore(LocalDateTime.now().minusMinutes(1));
        cartRepository.deleteAll(toBeDeleted);
    }
}
