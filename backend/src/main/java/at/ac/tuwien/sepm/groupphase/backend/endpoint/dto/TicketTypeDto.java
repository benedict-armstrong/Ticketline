package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
//@JsonIdentityInfo(scope = TicketTypeDto.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TicketTypeDto {

    private Long id;

    @NotBlank(message = "Title can't be empty")
    private String title;

    @NotNull(message = "Price multiplier is missing")
    @PositiveOrZero(message = "Price multiplier has to be at least zero")
    private Double multiplier;

}
