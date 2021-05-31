package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(uses = {LayoutUnitMapper.class, VenueMapper.class})
public interface TicketMapper {

    TicketDto ticketToTicketDto(Ticket ticket);

    Ticket ticketDtoToTicket(TicketDto ticketDto);

}
