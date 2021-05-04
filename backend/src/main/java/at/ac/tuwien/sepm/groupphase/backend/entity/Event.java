package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        if (!(o instanceof News)) {
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Event{"
            + "id=" + id
            + '}';
    }

    public static final class EventBuilder {
        private Long id;

        private EventBuilder() {
        }

        public static EventBuilder anEvent() {
            return new EventBuilder();
        }

        public EventBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            return event;
        }
    }
}
