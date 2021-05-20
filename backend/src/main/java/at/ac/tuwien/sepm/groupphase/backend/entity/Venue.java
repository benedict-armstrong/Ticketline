package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;

@Entity
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

}
