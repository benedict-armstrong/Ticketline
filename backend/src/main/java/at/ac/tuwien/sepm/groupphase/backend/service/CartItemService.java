package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;

import java.util.List;

public interface CartItemService {

    /**
     * Saves a order in the database.
     *
     * @param cartItem the order to be saved.
     * @param status the status should be saved in
     * @return the newly added order.
     */
    CartItem save(CartItem cartItem, CartItem.Status status);

    /**
     * Gets all order of a user with the given status.
     *
     * @param status the status should be searched in
     * @return list of cartItems.
     */
    List<CartItem> getCartItems(CartItem.Status status);

    /**
     * Adds all the cartItems in the users cart to a booking entity and changes their status to PAID_FOR.
     *
     * @return true if successfully
     */
    boolean checkout();

    /**
     * Deletes a order and all included tickets.
     *
     * @param id of the order
     * @return true if the order has been deleted, else false
     */
    boolean delete(Long id);

    /**
     * Deletes all cartItems in the cart that are to old.
     */
    void pruneCartItems();

}
