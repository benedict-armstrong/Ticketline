package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
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

    @NotNull
    private EventDto eventDto;

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

    public EventDto getEvent() {
        return event;
    }

    public void setEvent(EventDto eventDto) {
        this.eventDto = eventDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewsDto)) {
            return false;
        }
        NewsDto news = (NewsDto) o;
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


    public static final class NewsDtoBuilder {
        private Long id;
        private LocalDateTime publishedAt;
        private String author;
        private String title;
        private String text;
        private EventDto eventDto;

        private NewsDtoBuilder() {
        }

        public static NewsDtoBuilder aNews() {
            return new NewsDtoBuilder();
        }

        public NewsDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public NewsDtoBuilder withPublishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public NewsDtoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public NewsDtoBuilder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public NewsDtoBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public NewsDtoBuilder withEvent(EventDto eventDto) {
            this.eventDto = eventDto;
            return this;
        }

        public NewsDto build() {
            NewsDto newsDto = new NewsDto();
            newsDto.setId(id);
            newsDto.setPublishedAt(publishedAt);
            newsDto.setAuthor(author);
            newsDto.setTitle(title);
            newsDto.setText(text);
            newsDto.setEvent(eventDto);
            return newsDto;
        }
    }
}