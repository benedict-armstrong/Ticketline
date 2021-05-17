package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDto {

    private Long id;

    private LocalDateTime publishedAt;

    @NotBlank(message = "A first name is required")
    @Size(max = 100, message = "Name must be 100 characters or less")
    private String author;

    @NotBlank(message = "A title is required")
    @Size(max = 100, message = "Title must be 100 characters or less")
    private String title;

    @NotBlank(message = "A text is required")
    @Size(max = 10000, message = "Information text must be 10000 characters or less")
    private String text;

    @NotNull(message = "Event is required")
    private EventDto event;

    @Size(max = 10, message = "Upload 10 images or less")
    private FileDto[] images = new FileDto[10];
}