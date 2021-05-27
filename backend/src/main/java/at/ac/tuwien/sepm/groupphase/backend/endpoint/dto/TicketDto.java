package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    private Long id;

    private Long ownerId;

    @NotNull(message = "Ticket type is missing")
    private TicketTypeDto ticketType;

    private PerformanceDto performance;

    private List<Long> seats;

    private String status;
}
