package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

public class EventDto {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDto)) {
            return false;
        }
        EventDto news = (EventDto) o;
        return Objects.equals(id, news.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "News{"
            + "id=" + id
            + '}';
    }

    public static final class EventDtoBuilder {
        private Long id;

        private EventDtoBuilder() {
        }

        public static EventDtoBuilder anEvent() {
            return new EventDtoBuilder();
        }

        public EventDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EventDto build() {
            EventDto eventDto = new EventDto();
            eventDto.setId(id);
            return eventDto;
        }
    }
}