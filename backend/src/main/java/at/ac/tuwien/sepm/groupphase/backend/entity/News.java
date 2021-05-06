package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 10000)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CustomImage> customImages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<CustomImage> getCustomImages() {
        return customImages;
    }

    public void setCustomImages(Set<CustomImage> customImages) {
        this.customImages = customImages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof News)) {
            return false;
        }
        News news = (News) o;
        return Objects.equals(id, news.id)
            && Objects.equals(publishedAt, news.publishedAt)
            && Objects.equals(author, news.author)
            && Objects.equals(title, news.title)
            && Objects.equals(text, news.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publishedAt, author, title, text);
    }

    @Override
    public String toString() {
        return "News{"
            + "id=" + id
            + ", publishedAt=" + publishedAt
            + ", author='" + author + '\''
            + ", title='" + title + '\''
            + ", text='" + text + '\''
            + '}';
    }


    public static final class NewsBuilder {
        private Long id;
        private LocalDateTime publishedAt;
        private String author;
        private String title;
        private String text;
        private Event event;
        private Set<CustomImage> customImages = new HashSet<>();

        private NewsBuilder() {
        }

        public static NewsBuilder aNews() {
            return new NewsBuilder();
        }

        public NewsBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public NewsBuilder withPublishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public NewsBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public NewsBuilder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public NewsBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public NewsBuilder withEvent(Event event) {
            this.event = event;
            return this;
        }

        public NewsBuilder withImages(Set<CustomImage> customImages){
            this.customImages = customImages;
            return this;
        }

        public News build() {
            News news = new News();
            news.setId(id);
            news.setPublishedAt(publishedAt);
            news.setAuthor(author);
            news.setTitle(title);
            news.setText(text);
            news.setEvent(event);
            news.setCustomImages(customImages);
            return news;
        }
    }
}