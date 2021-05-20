package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueDto {

    @NotBlank(message = "A name is required")
    private String name;

    @NotNull
    private AddressDto addressDto;

    @NotNull
    private List<List<LayoutUnitDto>> layout;
}
