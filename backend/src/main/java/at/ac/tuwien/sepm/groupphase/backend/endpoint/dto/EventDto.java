package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

public class EventDto {

    private Long id;

    @NotBlank(message = "A title is required")
    @Size(max = 100, message = "Title must be 100 characters or less")
    private String title;

    @NotBlank(message = "A description is required")
    @Size(max = 10000, message = "Description must be 10000 characters or less")
    private String description;

    @Size(max = 10, message = "Upload 10 images or less")
    private FileDto[] images = new FileDto[10];

    private int duration;

    @NotNull(message = "Event date is required")
    @Future
    private LocalDateTime date;

    public FileDto[] getImages() {
        return images;
    }

    public void setImages(FileDto[] images) {
        this.images = images;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDto)) {
            return false;
        }
        EventDto event = (EventDto) o;
        return Objects.equals(id, event.id)
            && Objects.equals(title, event.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Event{"
            + "id=" + id
            + ", title=" + title
            + '}';
    }

    public static final class EventDtoBuilder {
        private Long id;
        private String title;

        private EventDtoBuilder() {
        }

        public static EventDtoBuilder aEvent() {
            return new EventDtoBuilder();
        }

        public EventDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EventDtoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            event.setTitle(title);
            return event;
        }
    }
}