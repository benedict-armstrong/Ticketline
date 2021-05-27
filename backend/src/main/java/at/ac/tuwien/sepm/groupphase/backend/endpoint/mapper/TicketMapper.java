package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TicketMapper {

    TicketDto ticketToTicketDto(Ticket ticket);

    List<TicketDto> ticketListToTicketDtoList(List<Ticket> tickets);

    Ticket ticketDtoToTicket(TicketDto ticketDto);

    /*
    default Long map(ApplicationUser value) {

    default Long map(Performance value) {
        return value.getId();
    }

    default ApplicationUser mapToUser(Long value) {
        return ApplicationUser.builder().id(value).build();
    }

    default Performance mapToPerformance(Long value) {
        return Performance.builder().id(value).build();
    }
    */
}
