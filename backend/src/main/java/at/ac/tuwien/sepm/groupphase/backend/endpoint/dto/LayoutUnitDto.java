package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LayoutUnitDto {

    private Long id;

    @NotBlank()
    @Size(max = 10, message = "Custom label can't be more than 10 characters long.")
    private String customLabel;

    @NotNull
    private SectorDto sector;
}
