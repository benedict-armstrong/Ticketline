package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    private Long id;

    @NotNull(message = "Owner ID is missing")
    private Long owner;

    @NotNull(message = "Performance ID is missing")
    private Long performance; // TODO: change type to Performance

    @NotNull(message = "Sector type is missing")
    private SectorType sectorType;

    @NotNull(message = "List of seats is missing")
    private List<Long> seats; // TODO: map to Venue's seats

    @NotNull(message = "Ticket type is missing")
    private TicketType ticketType;

    @PositiveOrZero(message = "Total price must be at least zero")
    private Long totalPrice; // 1 = 100 Cents

    private String status;

}
