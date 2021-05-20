package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectorDto {

    private Long id;

    @NotBlank(message = "A name is required")
    @Size(max = 200, message = "Name must be 200 characters or less")
    private String name;

    @NotNull
    private Sector.SectorType type;

    @NotBlank(message = "A first name is required")
    @Size(max = 200, message = "First name must be 200 characters or less")
    private String description;


    @Pattern(regexp = "^#[A-F,a-f,0-9]{6}$", message = "A color is required and must be in Hex-Format (e.g. #5D59D3)")
    private String color;

}
