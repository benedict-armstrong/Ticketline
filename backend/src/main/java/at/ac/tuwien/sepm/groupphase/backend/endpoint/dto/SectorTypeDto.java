package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(scope = UserDto.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SectorTypeDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Range(min = 1, message = "Atleast 1 ticket per sector")
    private int numberOfTickets;

    @PositiveOrZero
    @NotNull
    private Long price;
}
