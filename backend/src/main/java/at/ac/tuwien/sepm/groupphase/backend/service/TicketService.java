package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;

import java.util.List;

public interface TicketService {

    /**
     * Saves a amount of tickets in the database.
     *
     * @param performance of the tickets that should be added to the cartItem to be saved.
     * @param ticketType of the tickets that should be added to the cartItem to be saved.
     * @param status the status should be saved in
     * @param amount of tickets that should be created in the cartItem
     * @return the newly added tickets.
     */
    List<Ticket> save(Performance performance, TicketType ticketType, Ticket.Status status, int amount);

    /**
     * Gets all cartItems of a user with the given status.
     *
     * @param status the status should be searched in
     * @return list of cartItems.
     */
    List<Ticket> getTickets(Ticket.Status status);

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
     * Deletes all cartItems in the cart that are to old.
     */
    void pruneTickets();
}
