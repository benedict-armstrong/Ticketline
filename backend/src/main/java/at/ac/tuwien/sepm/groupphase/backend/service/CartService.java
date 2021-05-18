package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;

import java.util.List;

public interface CartService {

    /**
     * Adds a list of cart items.
     *
     * @param cart to add
     * @return list of completed entries
     */
    List<CartItem> addCart(List<CartItem> cart);
}
