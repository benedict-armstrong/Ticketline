package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import org.mapstruct.Mapper;

@Mapper
public interface TicketTypeMapper {

    TicketTypeDto ticketTypeToTicketType(TicketType value);

}
