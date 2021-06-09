package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;

import java.util.List;

public interface CartItemService {

    /**
     * Saves a cartItem in the database.
     *
     * @param performance of the tickets that should be added to the cartItem to be saved.
     * @param ticketType of the tickets that should be added to the cartItem to be saved.
     * @param status the status should be saved in
     * @param amount of tickets that should be created in the cartItem
     * @return the newly added cartItem.
     */
    CartItem save(Performance performance, TicketType ticketType, CartItem.Status status, int amount);

    /**
     * Adds a new ticket to a cartItem (seat selection is automatic).
     *
     * @param id the id of the cartItem.
     * @return the cartItem with the new ticket.
     */
    CartItem addTicket(Long id);

    /**
     * Gets all cartItems of a user with the given status.
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
     * Deletes a cartItem and all included tickets.
     *
     * @param id of the cartItem
     * @return true if the cartItem has been deleted, else false
     */
    boolean delete(Long id);

    /**
     * Deletes a ticket from a cartItem.
     *
     * @param id of the cartItem
     * @param ticketId of the ticket
     * @return true if the ticket has been deleted, else false
     */
    boolean deleteTicket(Long id, Long ticketId);

    /**
     * Deletes all cartItems in the cart that are to old.
     */
    void pruneCartItems();
}
