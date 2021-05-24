package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;

import java.util.List;

public interface CartService {

    /**
     * Adds a item to the cart.
     *
     * @param cartItem to add
     * @return the persisted cartItem
     */
    CartItem addCart(CartItem cartItem);

    /**
     * Returns a list of all CartItems of a user with the given id.
     * @param userId id of the user
     * @return List containing all the cart items
     */
    List<CartItem> getCart(Long userId);
}
