package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event.EventType;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public AddressDto getLocation() {
        return location;
    }

    public void setLocation(AddressDto location) {
        this.location = location;
    }

    public ArtistDto getArtist() {
        return artist;
    }

    public void setArtist(ArtistDto artist) {
        this.artist = artist;
    }

    public SectorTypeDto[] getSectorTypes() {
        return sectorTypes;
    }

    public void setSectorTypes(SectorTypeDto[] sectorTypes) {
        this.sectorTypes = sectorTypes;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventDto eventDto = (EventDto) o;
        return duration == eventDto.duration
            && Objects.equals(id, eventDto.id)
            && Objects.equals(title, eventDto.title)
            && Objects.equals(description, eventDto.description)
            && Arrays.equals(images, eventDto.images)
            && eventType == eventDto.eventType
            && Objects.equals(location, eventDto.location)
            && Objects.equals(artist, eventDto.artist)
            && Arrays.equals(sectorTypes, eventDto.sectorTypes)
            && Objects.equals(date, eventDto.date);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, eventType, duration, location, artist, date);
        result = 31 * result + Arrays.hashCode(images);
        result = 31 * result + Arrays.hashCode(sectorTypes);
        return result;
    }

    @Override
    public String toString() {
        return "EventDto{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", description='" + description + '\''
            + ", images=" + Arrays.toString(images)
            + ", eventType=" + eventType
            + ", duration=" + duration
            + ", locationDto=" + location
            + ", artistDto=" + artist
            + ", sectorTypeDto=" + Arrays.toString(sectorTypes)
            + ", date=" + date
            + '}';
    }

    public static final class EventDtoBuilder {

        private Long id;
        private @NotBlank(message = "A title is required") @Size(max = 100, message = "Title must be 100 characters or less") String title;
        private @NotBlank(message = "A description is required") @Size(max = 10000, message = "Description must be 10000 characters or less") String description;
        private @Size(max = 10, message = "Upload 10 images or less") FileDto[] images;
        private @NotBlank(message = "Eventtype is required") EventType eventType;
        private @Range(min = 1, message = "Duration must be greater than 0") int duration;
        private @NotNull(message = "Event date is required") @Future LocalDateTime date;

        private EventDtoBuilder() {
        }

        public static EventDtoBuilder aEvent() {
            return new EventDtoBuilder();
        }

        public EventDto build() {
            EventDto event = new EventDto();
            event.setId(id);
            event.setTitle(title);
            event.setEventType(eventType);
            event.setDuration(duration);
            event.setDescription(description);
            event.setImages(images);
            event.setDate(date);

            return event;
        }

        public EventDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EventDtoBuilder withTitle(@NotBlank(message = "A title is required") @Size(max = 100, message = "Title must be 100 characters or less") String title) {
            this.title = title;
            return this;
        }

        public EventDtoBuilder withDescription(@NotBlank(message = "A description is required") @Size(max = 10000, message = "Description must be 10000 characters or less") String description) {
            this.description = description;
            return this;
        }

        public EventDtoBuilder withImages(@Size(max = 10, message = "Upload 10 images or less") FileDto[] images) {
            this.images = images;
            return this;
        }

        public EventDtoBuilder withEventType(@NotBlank(message = "Eventtype is required") EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventDtoBuilder withDuration(@Range(min = 1, message = "Duration must be greater than 0") int duration) {
            this.duration = duration;
            return this;
        }

        public EventDtoBuilder withDate(@NotNull(message = "Event date is required") @Future LocalDateTime date) {
            this.date = date;
            return this;
        }
    }
}