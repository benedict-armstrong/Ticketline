package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDto {
    private Long id;

    @NotBlank(message = "A first name is required")
    @Size(max = 200, message = "First name must be 200 characters or less")
    private String firstName;

    @Size(max = 200, message = "Last name must be 200 characters or less")
    private String lastName;

    private PerformanceDto[] performances;
}
