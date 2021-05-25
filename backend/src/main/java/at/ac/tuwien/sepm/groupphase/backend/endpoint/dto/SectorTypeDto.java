package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectorTypeDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Range(min = 1, message = "Atleast 1 ticket per sector")
    private int numberOfTickets;
}
