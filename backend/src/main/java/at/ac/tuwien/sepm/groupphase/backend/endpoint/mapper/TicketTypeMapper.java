package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import org.mapstruct.Mapper;

@Mapper(uses = {SectorTypeMapper.class})
public interface TicketTypeMapper {

    TicketTypeDto ticketTypeToTicketTypeDto(TicketType value);

    TicketType ticketTypeDtoToTicketType(TicketTypeDto value);

}
