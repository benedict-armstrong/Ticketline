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
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LayoutUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String customLabel;

    @ManyToOne(fetch = FetchType.EAGER)
    private Sector sector;

    @Column(nullable = false)
    private int localId;

    @Column
    private Boolean taken;
}
