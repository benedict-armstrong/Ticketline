package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "A description is required")
    @Size(max = 10000, message = "Description must be 10000 characters or less")
    private String description;

    @NotNull
    @Size(min = 1, message = "Event must have atleast one performance")
    private PerformanceDto[] performances;

    @Range(min = 1, message = "Duration must be greater than 0")
    private int duration;

    @NotNull(message = "Eventtype is required")
    private Event.EventType eventType;

    @NotNull
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;

    @NotNull
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Size(max = 10, message = "Upload 10 images or less")
    @Size(min = 1, message = "Atleast one image required")
    @ToString.Exclude
    private FileDto[] images = new FileDto[10];
}
