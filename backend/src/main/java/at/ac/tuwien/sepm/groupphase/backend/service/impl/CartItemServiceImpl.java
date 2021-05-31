package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.repository.CartItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.CartItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@EnableScheduling
public class CartItemServiceImpl implements CartItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final BookingService bookingService;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               UserRepository userRepository,
                               AuthenticationFacade authenticationFacade,
                               BookingService bookingService) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
        this.bookingService = bookingService;
    }

    @Override
    public CartItem save(CartItem cartItem, CartItem.Status status) {
        LOGGER.trace("save({})", cartItem);
        cartItem.setUser(
            userRepository.findUserByEmail(authenticationFacade.getMail())
        );
        cartItem.setStatus(status);
        cartItem.setChangeDate(LocalDateTime.now());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getCartItems(CartItem.Status status) {
        LOGGER.trace("getCartItems({})", status);
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        return cartItemRepository.findByUserAndStatus(user, status);
    }

    @Override
    public boolean checkout() {
        LOGGER.trace("checkout()");
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        List<CartItem> cartItems = cartItemRepository.findByUserAndStatus(user, CartItem.Status.IN_CART);
        if (cartItems.size() > 0) {
            for (CartItem cartItem : cartItems) {
                cartItem.setStatus(CartItem.Status.PAID_FOR);
            }
            cartItemRepository.saveAll(cartItems);

            Booking booking = Booking.builder().cartItems(new HashSet<>(cartItems)).build();
            bookingService.save(booking);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        LOGGER.trace("delete({})", id);
        cartItemRepository.deleteById(id);
        return true;
    }

    @Override
    public void pruneCartItems() {
        LOGGER.trace("pruneCartItems()");
        List<CartItem> toBeDeleted = cartItemRepository.findByChangeDateBeforeAndStatus(LocalDateTime.now().minusMinutes(1), CartItem.Status.IN_CART);
        if (toBeDeleted.size() > 0) {
            LOGGER.info("Deleting {} stale tickets from carts", toBeDeleted.size());
        }
        cartItemRepository.deleteAll(toBeDeleted);
    }
}
