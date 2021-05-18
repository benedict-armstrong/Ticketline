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
public class CartItemDto {

    private Long id;

    @NotNull(message = "User is required")
    private UserDto userDto;

    @NotNull(message = "Event is required")
    private EventDto eventDto;

    @NotNull(message = "Sector Type is required")
    private SectorTypeDto sectorTypeDto;

    @NotNull(message = "Amount is required")
    private Integer amount;
}
