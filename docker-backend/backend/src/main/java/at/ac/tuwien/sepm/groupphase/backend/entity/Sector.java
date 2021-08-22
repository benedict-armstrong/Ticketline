package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long localId;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private SectorType type;

    @Column(length = 1000)
    private String description;

    //color in hex format i.e. #E3F54C
    @Column(nullable = false, length = 10)
    private String color;

    public enum SectorType {
        SEATED, STANDING, STAGE
    }

}
