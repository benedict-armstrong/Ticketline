package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    private Long id;

    @NotNull(message = "Performance ID is missing")
    private PerformanceDto performance;

    @NotNull(message = "Seat is required")
    private LayoutUnitDto seat;

    @NotNull(message = "Ticket type is missing")
    private TicketTypeDto ticketType;

    private String status;
}
