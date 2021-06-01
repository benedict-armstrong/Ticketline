package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.validation.CustomLayoutConstraint;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private AddressDto address;

    @NotNull
    @Size(min = 1)
    private List<SectorDto> sectors;

    @CustomLayoutConstraint(message = "Layout must be a well formed matrix")
    private List<List<LayoutUnitDto>> layout;
}
