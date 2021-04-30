package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "line_one")
    private String lineOne;

    @Column(name = "line_two")
    private String lineTwo;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postcode;

    @Column(nullable = false)
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return id.equals(address.id)
            && name.equals(address.name)
            && lineOne.equals(address.lineOne)
            && Objects.equals(lineTwo, address.lineTwo)
            && city.equals(address.city)
            && postcode.equals(address.postcode)
            && country.equals(address.country);
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


    public static final class AddressBuilder {
        private Long id;
        private String name;
        private String lineOne;
        private String lineTwo;
        private String city;
        private String postcode;
        private String country;

        private AddressBuilder() {
        }

        public static AddressBuilder anAddress() {
            return new AddressBuilder();
        }

        public AddressBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AddressBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public AddressBuilder withLineOne(String lineOne) {
            this.lineOne = lineOne;
            return this;
        }

        public AddressBuilder withLineTwo(String lineTwo) {
            this.lineTwo = lineTwo;
            return this;
        }

        public AddressBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder withPostcode(String postcode) {
            this.postcode = postcode;
            return this;
        }

        public AddressBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            Address address = new Address();
            address.setId(id);
            address.setName(name);
            address.setLineOne(lineOne);
            address.setLineTwo(lineTwo);
            address.setCity(city);
            address.setPostcode(postcode);
            address.setCountry(country);
            return address;
        }
    }
}
