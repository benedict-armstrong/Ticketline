package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;

import java.util.List;

public interface TicketService {

    /**
     * Saves a amount of tickets in the database.
     *
     * @param performance of the tickets to be saved
     * @param ticketType of the tickets to be saved
     * @param status the status should be saved in
     * @param amount of tickets that should be created
     * @return the newly added tickets.
     */
    List<Ticket> save(Performance performance, TicketType ticketType, Ticket.Status status, int amount);

    /**
     * Gets all tickets of a user with the given status.
     *
     * @param status the status should be searched in
     * @return list of tickets.
     */
    List<Ticket> getTickets(Ticket.Status status);

    /**
     * Adds all the tickets in the users cart to a booking entity and changes their status to PAID_FOR.
     *
     * @return true if successfully
     */
    boolean checkout();

    /**
     * Adds all the tickets in the users cart to a booking entity and changes their status to RESERVED.
     *
     * @return true if successfully
     */
    boolean reserve();

    /**
     * Deletes a ticket.
     *
     * @param id of the ticket
     * @return true if the ticket has been deleted, else false
     */
    boolean delete(Long id);

    /**
     * Deletes a list of tickets.
     *
     * @param ids of the tickets
     * @return true if the tickets have been deleted, false if no tickets were deleted
     */
    boolean delete(List<Long> ids);

    /**
     * Deletes all tickets in the cart that are to old.
     */
    void pruneTickets();
}
