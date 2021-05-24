package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;

public interface CartService {

    /**
     * Adds a item to the cart.
     *
     * @param cartItem to add
     * @return the persisted cartItem
     */
    CartItem addCart(CartItem cartItem);
}
