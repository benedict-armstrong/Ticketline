package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public enum EventType {
        CINEMA, THEATRE, OPERA, CONCERT
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Column(nullable = false)
    private int duration;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<File> images = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<File> getImages() {
        return images;
    }

    public void setImages(Set<File> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return duration == event.duration && Objects.equals(id, event.id) && title.equals(event.title) && description.equals(event.description) && date.equals(event.date) && Objects.equals(images, event.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, date, duration, images);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", description='" + description + '\''
            + ", date=" + date
            + ", duration=" + duration
            + ", images=" + images
            + '}';
    }

    public static final class EventBuilder {
        private Long id;
        private String title;
        private String description;
        private LocalDateTime date;
        private EventType eventType;
        private int duration;
        private Set<File> images;

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

        public EventBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder withDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public EventBuilder withEventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventBuilder withDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public EventBuilder withImages(Set<File> images) {
            this.images = images;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            event.setTitle(title);
            event.setDescription(description);
            event.setDate(date);
            event.setEventType(eventType);
            event.setDuration(duration);
            event.setImages(images);
            return event;
        }
    }
}
