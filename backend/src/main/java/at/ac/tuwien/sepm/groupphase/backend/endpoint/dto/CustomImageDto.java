package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class CustomImageDto {

    private Long id;
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
        if (!(o instanceof CustomImageDto)) {
            return false;
        }
        CustomImageDto event = (CustomImageDto) o;
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

    public static final class CustomImageDtoBuilder {
        private Long id;
        private byte[] imageData;

        private CustomImageDtoBuilder() {
        }

        public static CustomImageDtoBuilder aImage() {
            return new CustomImageDtoBuilder();
        }

        public CustomImageDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CustomImageDtoBuilder withImageData(byte[] imageData) {
            this.imageData = imageData;
            return this;
        }

        public CustomImageDto build() {
            CustomImageDto image = new CustomImageDto();
            image.setId(id);
            image.setImageData(imageData);
            return image;
        }
    }
}
