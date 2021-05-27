package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper
public interface TicketStatusMapper {

    default Ticket.Status modeToStatus(String value) {
        switch (value) {
            case "buy":
                return Ticket.Status.PAID_FOR;
            case "reserve":
                return Ticket.Status.RESERVED;
            default:
                return null;
        }
    }

}
