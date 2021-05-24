package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private Long id;

    @NotNull(message = "User is required")
    private Long userId;

    @NotNull(message = "TicketType is required")
    private Long ticketId;

    @NotNull(message = "Amount is required")
    @Min(0)
    private Integer amount;
}
