package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
//@JsonIdentityInfo(scope = PerformanceDto.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @Size(min = 1, message = "At least one sector type is required")
    private SectorTypeDto[] sectorTypes;

    @Size(min = 1, message = "At least one ticket type is required")
    private TicketTypeDto[] ticketTypes;

    @NotNull(message = "Performance date is required")
    @Future
    private LocalDateTime date;
}