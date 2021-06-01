package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime buyDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ApplicationUser user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @NotNull
    private Set<CartItem> cartItems = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private File invoice;
}