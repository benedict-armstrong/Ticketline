package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface TicketTypeMapper {

    TicketTypeDto ticketTypeToTicketTypeDto(TicketType value);

    TicketType ticketTypeDtoToTicketType(TicketTypeDto value);

    TicketTypeDto[] ticketTypeSetToTicketTypeDtoArray(Set<TicketType> ticketTypeSet);

    Set<TicketType> ticketTypeDtoArrayToTicketTypeSet(TicketTypeDto[] ticketTypeDtos);

}
