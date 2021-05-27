package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceDto {

    private Long id;

    @NotBlank(message = "A title is required")
    @Size(max = 100, message = "Title must be 100 characters or less")
    private String title;

    @NotBlank(message = "A description is required")
    @Size(max = 10000, message = "Description must be 10000 characters or less")
    private String description;

    @NotNull(message = "Location is required")
    private AddressDto location;

    @NotNull(message = "Artist is required")
    private ArtistDto artist;

    @Size(min = 1, message = "Atleast one sectortype is required")
    private SectorTypeDto[] sectorTypes;

    @NotNull(message = "Performance date is required")
    @Future
    private LocalDateTime date;
}