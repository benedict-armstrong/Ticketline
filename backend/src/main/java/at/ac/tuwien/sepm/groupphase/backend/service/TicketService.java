package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;

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
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no ticket is found.
     */
    Ticket cancel(Long id);

}
