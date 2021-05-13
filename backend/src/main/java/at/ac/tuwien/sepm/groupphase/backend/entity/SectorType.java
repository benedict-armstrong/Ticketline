package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import java.util.Objects;

@Entity
public class SectorType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    int amountOfTickets;

    public String getName() {
        return name;
    }

    public int getAmountOfTickets() {
        return amountOfTickets;
    }

    public void setAmountOfTickets(int amountOfTickets) {
        this.amountOfTickets = amountOfTickets;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectorType that = (SectorType) o;
        return amountOfTickets == that.amountOfTickets && Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amountOfTickets);
    }

    @Override
    public String toString() {
        return "SectorType{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", amountOfTickets=" + amountOfTickets
            + '}';
    }

    public static final class SectorTypeBuilder {
        private Long id;
        private String name;
        private int amountOfTickets;

        private SectorTypeBuilder() {}

        public static SectorTypeBuilder aSectorType() {
            return new SectorTypeBuilder();
        }

        public SectorTypeBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SectorTypeBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SectorTypeBuilder withAmountOfTickets(int amountOfTickets) {
            this.amountOfTickets = amountOfTickets;
            return this;
        }

        public SectorType build() {
            SectorType sectorType = new SectorType();
            sectorType.setId(id);
            sectorType.setName(name);
            sectorType.setAmountOfTickets(amountOfTickets);
            return sectorType;
        }
    }
}
