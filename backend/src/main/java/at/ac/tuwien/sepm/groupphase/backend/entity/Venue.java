package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private int width;

    @OneToOne(cascade = {CascadeType.ALL})
    private Address address;

    @OneToMany
    private List<Sector> sectors;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<LayoutUnit> layout;

}
