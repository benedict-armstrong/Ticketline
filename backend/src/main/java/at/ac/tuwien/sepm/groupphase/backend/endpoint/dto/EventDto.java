package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {

    private Long id;

    @NotBlank(message = "A title is required")
    @Size(max = 100, message = "Title must be 100 characters or less")
    private String title;

    @NotBlank(message = "A description is required")
    @Size(max = 10000, message = "Description must be 10000 characters or less")
    private String description;

    @Size(max = 10, message = "Upload 10 images or less")
    @Size(min = 1, message = "Atleast one image required")
    private FileDto[] images = new FileDto[10];

    @NotNull(message = "Eventtype is required")
    private EventType eventType;

    @Range(min = 1, message = "Duration must be greater than 0")
    private int duration;

    @NotNull(message = "Location is required")
    private AddressDto location;

    @NotNull(message = "Artist is required")
    private ArtistDto artist;

    @Size(min = 1, message = "Atleast one sectortype is required")
    private SectorTypeDto[] sectorTypes;

    @NotNull(message = "Event date is required")
    @Future
    private LocalDateTime date;
}