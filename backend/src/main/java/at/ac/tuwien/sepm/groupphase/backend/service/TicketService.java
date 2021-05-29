package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;

import java.util.List;

public interface TicketService {

    /**
     * Saves a ticket in the database.
     *
     * @param ticket the ticket to be saved.
     * @return the newly added ticket.
     */
    Ticket save(Ticket ticket, Ticket.Status status);

    /**
     * Cancels an already existing ticket.
     *
     * @param id the id of the ticket to be cancelled.
     * @return the new ticket.
     */
    Ticket cancel(Long id);

    /**
     * Gets all tickets in the cart of a User.
     *
     * @return list of tickets.
     */
    List<Ticket> getCartTickets();

    /**
     * Updates the seats of a ticket with the given id.
     *
     * @param ticketUpdate entity containing the id and the new seats.
     * @return entity containing the id and the new seats.
     */
    TicketUpdateDto updateSeats(TicketUpdateDto ticketUpdate);

    /**
     * Deletes a ticket.
     *
     * @param id of the ticket
     * @return true if the ticket has been deleted, else false
     */
    boolean delete(Long id);

    /**
     * Deletes all tickets in the cart that are to old.
     */
    void pruneTicketsInCart();
}
