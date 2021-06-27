package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;

public interface TicketTypeService {

    /**
     * Get ticketType by id.
     *
     * @param id id of TicketType
     * @return TicketType
     */
    TicketType getTicketTypeById(long id);
}
