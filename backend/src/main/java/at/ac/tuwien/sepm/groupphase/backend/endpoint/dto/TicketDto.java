package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
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

    //@JsonIdentityReference(alwaysAsId = true)
    private UserDto owner;

    //@JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Performance ID is missing")
    private PerformanceDto performance;

    //@JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Sector type is missing")
    private SectorTypeDto sectorType;

    //@NotNull(message = "List of seats is missing")
    private List<Long> seats; // TODO: map to Venue's seats

    //@JsonIdentityReference(alwaysAsId = true)
    @NotNull(message = "Ticket type is missing")
    private TicketTypeDto ticketType;

    @PositiveOrZero(message = "Total price must be at least zero")
    private Long totalPrice; // 1 = 100 Cents

    private String status;

}
