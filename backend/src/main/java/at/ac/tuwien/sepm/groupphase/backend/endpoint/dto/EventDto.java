/*
package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

import java.util.Objects;

public class EventDto {

    private Long id;

    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
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

 */