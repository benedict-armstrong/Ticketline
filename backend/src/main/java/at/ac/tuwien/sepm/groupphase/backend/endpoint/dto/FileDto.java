package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {

    private Long id;
    @ToString.Exclude
    private byte[] data;
    private String type;
}
