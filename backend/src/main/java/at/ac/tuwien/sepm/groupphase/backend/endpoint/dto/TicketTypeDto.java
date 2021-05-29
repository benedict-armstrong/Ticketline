package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketTypeDto {

    private Long id;

    @NotBlank(message = "Title can't be empty")
    private String title;

    @PositiveOrZero
    @NotNull
    private Long price;

    @NotNull
    private SectorTypeDto sectorType;
}
