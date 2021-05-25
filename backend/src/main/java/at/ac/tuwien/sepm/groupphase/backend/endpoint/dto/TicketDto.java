package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

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

    private UserDto owner;

    @NotNull(message = "Performance ID is missing")
    private PerformanceDto performance;

    @NotNull(message = "Sector type is missing")
    private SectorTypeDto sectorType;

    private List<Long> seats; // TODO: map to Venue's seats

    @NotNull(message = "Ticket type is missing")
    private TicketTypeDto ticketType;

    @PositiveOrZero(message = "Total price must be at least zero")
    private Long totalPrice; // 1 = 100 Cents

    private String status;

}
