package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
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

    @Column(nullable = false)
    private int duration;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "LOCATION_ID")
    private Address location;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ARTIST_ID")
    private Artist artist;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<File> images = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotNull
    private Set<SectorType> sectorTypes = new HashSet<>();

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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Set<SectorType> getSectorTypes() {
        return sectorTypes;
    }

    public void setSectorTypes(Set<SectorType> sectorTypes) {
        this.sectorTypes = sectorTypes;
    }

    public Address getLocation() {
        return location;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setLocation(Address location) {
        this.location = location;
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
        return duration == event.duration
            && Objects.equals(id, event.id)
            && Objects.equals(title, event.title)
            && Objects.equals(description, event.description)
            && Objects.equals(date, event.date) && eventType == event.eventType
            && Objects.equals(location, event.location)
            && Objects.equals(artist, event.artist)
            && Objects.equals(images, event.images)
            && Objects.equals(sectorTypes, event.sectorTypes);
    }

    @Override
    public String toString() {
        return "Event{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", description='" + description + '\''
            + ", date=" + date
            + ", eventType=" + eventType
            + ", duration=" + duration
            + ", location=" + location
            + ", artist=" + artist
            + ", images=" + images
            + ", sectorTypes=" + sectorTypes
            + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, date, eventType, duration, location, artist, images, sectorTypes);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final class EventBuilder {
        private Long id;
        private String title;
        private String description;
        private LocalDateTime date;
        private Artist artist;
        private Address location;
        private EventType eventType;
        private int duration;
        private Set<File> images;
        private Set<SectorType> sectorTypes;

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

        public EventBuilder withSectorTypes(Set<SectorType> sectorTypes) {
            this.sectorTypes = sectorTypes;
            return this;
        }

        public EventBuilder withArtist(Artist artist) {
            this.artist = artist;
            return this;
        }

        public EventBuilder withLocation(Address location) {
            this.location = location;
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
            event.setArtist(artist);
            event.setLocation(location);
            event.setSectorTypes(sectorTypes);
            return event;
        }
    }
}