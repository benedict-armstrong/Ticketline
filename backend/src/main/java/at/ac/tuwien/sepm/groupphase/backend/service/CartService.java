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
    CartItem addCartItem(CartItem cartItem);

    /**
     * Returns a list of all CartItems of a user with the given id.
     *
     * @param userId id of the user
     * @return List containing all the cart items
     */
    List<CartItem> getCart(Long userId);

    /**
     * Updates a item in the cart.
     *
     * @param cartItem to update
     * @return the persisted cartItem
     */
    CartItem updateCartItem(CartItem cartItem);

    /**
     * Deletes a item from the cart.
     *
     * @param cartItem to delete
     * @return true if the item has been deleted
     */
    boolean deleteCartItem(CartItem cartItem);
}
