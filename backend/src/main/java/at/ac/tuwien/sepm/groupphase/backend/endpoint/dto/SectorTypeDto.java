package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class SectorTypeDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Range(min = 1, message = "Atleast 1 ticket per sector")
    private int numberOfTickets;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectorTypeDto that = (SectorTypeDto) o;
        return numberOfTickets == that.numberOfTickets && Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfTickets);
    }

    @Override
    public String toString() {
        return "SectorTypeDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", amountOfTickets=" + numberOfTickets
            + '}';
    }

    public static final class SectorTypeDtoBuilder {
        private Long id;
        private String name;
        private int numberOfTickets;

        private SectorTypeDtoBuilder() {
        }

        public static SectorTypeDtoBuilder aSectorTypeDto() {
            return new SectorTypeDtoBuilder();
        }

        public SectorTypeDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SectorTypeDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SectorTypeDtoBuilder withNumberOfTickets(int numberOfTickets) {
            this.numberOfTickets = numberOfTickets;
            return this;
        }

        public SectorTypeDto build() {
            SectorTypeDto sectorTypeDto = new SectorTypeDto();
            sectorTypeDto.setId(id);
            sectorTypeDto.setName(name);
            sectorTypeDto.setNumberOfTickets(numberOfTickets);
            return sectorTypeDto;
        }
    }
}
