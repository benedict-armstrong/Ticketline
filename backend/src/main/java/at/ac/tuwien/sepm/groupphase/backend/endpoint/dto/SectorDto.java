package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(scope=SectorDto.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class SectorDto {

    private Long id;

    @NotBlank(message = "A name is required")
    @Size(max = 200, message = "Name must be 200 characters or less")
    private String name;

    @NotNull
    private Sector.SectorType type;

    @Size(max = 200, message = "Description must be 200 characters or less")
    private String description;

    @Pattern(regexp = "^#[A-F,a-f,0-9]{6}$", message = "A color is required and must be in Hex-Format (e.g. #5D59D3)")
    private String color;

}
