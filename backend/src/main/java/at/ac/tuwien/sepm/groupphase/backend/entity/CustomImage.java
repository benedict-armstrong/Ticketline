package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class CustomImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private byte[] imageData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomImage)) {
            return false;
        }
        CustomImage event = (CustomImage) o;
        return Objects.equals(id, event.id)
            && Objects.equals(imageData, event.imageData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Image{"
            + "id=" + id
            + ", imageData=" + imageData.toString()
            + '}';
    }

    public static final class CustomImageBuilder {
        private Long id;
        private byte[] imageData;

        private CustomImageBuilder() {
        }

        public static CustomImageBuilder aImage() {
            return new CustomImageBuilder();
        }

        public CustomImageBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CustomImageBuilder withImageData(byte[] imageData) {
            this.imageData = imageData;
            return this;
        }

        public CustomImage build() {
            CustomImage image = new CustomImage();
            image.setId(id);
            image.setImageData(imageData);
            return image;
        }
    }
}
