package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {TicketTypeMapper.class, UserMapper.class, PerformanceMapper.class})
public interface TicketMapper {

    TicketDto ticketToTicketDto(Ticket ticket);

    List<TicketDto> ticketListToTicketDtoList(List<Ticket> tickets);

    Ticket ticketDtoToTicket(TicketDto ticketDto);
}
