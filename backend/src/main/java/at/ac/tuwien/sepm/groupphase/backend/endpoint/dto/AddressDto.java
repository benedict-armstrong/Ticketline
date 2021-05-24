package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String lineOne;

    private String lineTwo;

    @NotNull
    @NotEmpty
    private String city;

    @NotNull
    @NotEmpty
    private String postcode;

    @NotNull
    @NotEmpty
    private String country;

    @NotNull
    private boolean eventLocation;

    private PerformanceDto[] performances;
}
