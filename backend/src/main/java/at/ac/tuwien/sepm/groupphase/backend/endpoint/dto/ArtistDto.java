package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class ArtistDto {
    private Long id;

    @NotBlank(message = "A first name is required")
    @Size(max = 200, message = "First name must be 200 characters or less")
    private String firstName;

    @NotBlank(message = "A last name is required")
    @Size(max = 200, message = "Last name must be 200 characters or less")
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArtistDto artistDto = (ArtistDto) o;
        return Objects.equals(id, artistDto.id) && Objects.equals(firstName, artistDto.firstName) && Objects.equals(lastName, artistDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    @Override
    public String toString() {
        return "ArtistDto{"
            + "id=" + id
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + '}';
    }

    public static final class ArtistDtoBuilder {
        private Long id;
        private String firstName;
        private String lastName;

        private ArtistDtoBuilder() {}

        public ArtistDtoBuilder anArtistDto() {
            return new ArtistDtoBuilder();
        }

        public ArtistDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ArtistDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ArtistDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ArtistDto build() {
            ArtistDto artistDto = new ArtistDto();
            artistDto.setId(id);
            artistDto.setFirstName(firstName);
            artistDto.setLastName(lastName);
            return artistDto;
        }
    }
}
