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
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String firstName;

    @Column(nullable = false, length = 200)
    private String lastName;

    @Column(nullable = false, length = 50, name = "tel_number")
    private String telephoneNumber;

    @Column(nullable = false, length = 200, unique = true)
    private String email;

    @Column(nullable = false, length = 500)
    private String password;

    @Column(nullable = false, name = "last_login")
    private LocalDateTime lastLogin;

    @OneToOne
    @JoinColumn(name = "last_read_news")
    private News lastReadNews;

    @Column()
    private int points;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserRole role;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Address address;

    public enum UserStatus {
        ACTIVE, BANNED
    }

    public enum UserRole {
        CLIENT, ORGANIZER, ADMIN
    }
}
