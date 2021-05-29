package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(uses = {TicketTypeMapper.class, UserMapper.class, PerformanceMapper.class})
public interface TicketMapper {

    TicketDto ticketToTicketDto(Ticket ticket);

    List<TicketDto> ticketListToTicketDtoList(List<Ticket> tickets);

    List<Ticket> ticketDtoListToTicketList(List<TicketDto> ticketDtos);

    Ticket ticketDtoToTicket(TicketDto ticketDto);

    Ticket[] ticketSetToTicketArray(Set<Ticket> set);

    Set<Ticket> ticketArrayToTicketSet(Ticket[] ticketArray);
}
