package at.ac.tuwien.sepm.groupphase.backend.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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

    @Column()
    private int points;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserStatus userStatus;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserRole userRole;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Address address;

    public enum UserStatus {
        ACTIVE, BANNED
    }

    public enum UserRole {
        CLIENT, ORGANIZER, ADMIN
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public UserStatus getStatus() {
        return userStatus;
    }

    public void setStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserRole getRole() {
        return userRole;
    }

    public void setRole(UserRole role) {
        this.userRole = role;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static final class UserBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String telephoneNumber;
        private String email;
        private String password;
        private LocalDateTime lastLogin;
        private int points;
        private UserStatus userStatus;
        private UserRole userRole;
        private Address address;

        private UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder withTelephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withLastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public UserBuilder withPoints(int points) {
            this.points = points;
            return this;
        }

        public UserBuilder withStatus(UserStatus userStatus) {
            this.userStatus = userStatus;
            return this;
        }

        public UserBuilder withRole(UserRole role) {
            this.userRole = role;
            return this;
        }

        public UserBuilder withAddress(Address address) {
            this.address = address;
            return this;
        }

        public ApplicationUser build() {
            ApplicationUser user = new ApplicationUser();
            user.email = this.email;
            user.lastName = this.lastName;
            user.password = this.password;
            user.firstName = this.firstName;
            user.userRole = this.userRole;
            user.lastLogin = this.lastLogin;
            user.points = this.points;
            user.telephoneNumber = this.telephoneNumber;
            user.address = this.address;
            user.id = this.id;
            user.userStatus = this.userStatus;
            return user;
        }
    }
}
