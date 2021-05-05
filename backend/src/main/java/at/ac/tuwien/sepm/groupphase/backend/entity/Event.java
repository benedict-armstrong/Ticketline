package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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
        if (!(o instanceof News)) {
            return false;
        }
        Event event = (Event) o;
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

    public static final class EventBuilder {
        private Long id;
        private String title;

        private EventBuilder() {
        }

        public static EventBuilder aEvent() {
            return new EventBuilder();
        }

        public EventBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EventBuilder withTitle(String title) {
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
