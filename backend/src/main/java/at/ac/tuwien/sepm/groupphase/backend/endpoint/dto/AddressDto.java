package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AddressDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String lineOne;

    private String lineTwo;

    @NotNull
    @NotEmpty
    private String city;

    @NotNull
    @NotEmpty
    private String postcode;

    @NotNull
    @NotEmpty
    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = lineTwo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDto that = (AddressDto) o;
        return Objects.equals(id, that.id) && name.equals(that.name) && lineOne.equals(that.lineOne) && Objects.equals(lineTwo, that.lineTwo) && city.equals(that.city) && postcode.equals(that.postcode) && country.equals(that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lineOne, lineTwo, city, postcode, country);
    }

    @Override
    public String toString() {
        return "Address{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", lineOne='" + lineOne + '\''
            + ", lineTwo='" + lineTwo + '\''
            + ", city='" + city + '\''
            + ", postcode='" + postcode + '\''
            + ", country='" + country + '\''
            + '}';
    }


    public static final class AddressDtoBuilder {
        private Long id;
        private String name;
        private String lineOne;
        private String lineTwo;
        private String city;
        private String postcode;
        private String country;

        private AddressDtoBuilder() {
        }

        public static AddressDtoBuilder anAddressDto() {
            return new AddressDtoBuilder();
        }

        public AddressDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AddressDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public AddressDtoBuilder withLineOne(String lineOne) {
            this.lineOne = lineOne;
            return this;
        }

        public AddressDtoBuilder withLineTwo(String lineTwo) {
            this.lineTwo = lineTwo;
            return this;
        }

        public AddressDtoBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AddressDtoBuilder withPostcode(String postcode) {
            this.postcode = postcode;
            return this;
        }

        public AddressDtoBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public AddressDto build() {
            AddressDto addressDto = new AddressDto();
            addressDto.setId(id);
            addressDto.setName(name);
            addressDto.setLineOne(lineOne);
            addressDto.setLineTwo(lineTwo);
            addressDto.setCity(city);
            addressDto.setPostcode(postcode);
            addressDto.setCountry(country);
            return addressDto;
        }
    }
}
