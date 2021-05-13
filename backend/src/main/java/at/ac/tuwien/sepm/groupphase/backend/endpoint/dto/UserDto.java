package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserDto  {

    private Long id;

    @NotBlank(message = "A first name is required")
    @Size(max = 200, message = "First name must be 200 characters or less")
    private String firstName;

    @NotBlank(message = "A last name is required")
    @Size(max = 200, message = "Last name must be 200 characters or less")
    private String lastName;

    @Size(max = 200, message = "Phone number can't be more than 50 characters long")
    private String telephoneNumber;

    @NotBlank(message = "An email is required")
    @Email
    @Size(max = 200, message = "Email must be 200 characters or less")
    private String email;

    @NotBlank(message = "Password is required and cant be empty")
    private String password;

    private LocalDateTime lastLogin;

    private NewsDto lastReadNews;

    @PositiveOrZero(message = "Negative points values are not allowed")
    private int points;

    private ApplicationUser.UserStatus status;

    private ApplicationUser.UserRole role;

    @NotNull
    private AddressDto addressDto;


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

    public NewsDto getLastReadNews() {
        return lastReadNews;
    }

    public void setLastReadNews(NewsDto lastReadNews) {
        this.lastReadNews = lastReadNews;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ApplicationUser.UserStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationUser.UserStatus userStatus) {
        this.status = userStatus;
    }

    public ApplicationUser.UserRole getRole() {
        return role;
    }

    public void setRole(ApplicationUser.UserRole role) {
        this.role = role;
    }

    public AddressDto getAddress() {
        return addressDto;
    }

    public void setAddress(AddressDto addressDto) {
        this.addressDto = addressDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto userDto = (UserDto) o;
        return points == userDto.points
            && Objects.equals(id, userDto.id)
            && firstName.equals(userDto.firstName)
            && lastName.equals(userDto.lastName)
            && Objects.equals(telephoneNumber, userDto.telephoneNumber)
            && email.equals(userDto.email)
            && password.equals(userDto.password)
            && Objects.equals(lastLogin, userDto.lastLogin)
            && status == userDto.status
            && role == userDto.role
            && addressDto.equals(userDto.addressDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, telephoneNumber, email, password, lastLogin, points, status, role, addressDto);
    }

    @Override
    public String toString() {
        return "UserDto{"
            + "id=" + id
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", telephoneNumber='" + telephoneNumber + '\''
            + ", email='" + email + '\''
            + ", password='" + password + '\''
            + ", lastLogin=" + lastLogin
            + ", lastReadNews" + lastReadNews.getId()
            + ", points=" + points
            + ", status=" + status
            + ", role=" + role
            + ", addressDto=" + addressDto
            + '}';
    }

    public static final class UserDtoBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String telephoneNumber;
        private String email;
        private String password;
        private LocalDateTime lastLogin;
        private int points;
        private ApplicationUser.UserStatus status;
        private ApplicationUser.UserRole role;
        private AddressDto addressDto;

        private UserDtoBuilder() {
        }

        public static UserDtoBuilder anUserDto() {
            return new UserDtoBuilder();
        }

        public UserDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserDtoBuilder withTelephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public UserDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserDtoBuilder withLastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public UserDtoBuilder withPoints(int points) {
            this.points = points;
            return this;
        }

        public UserDtoBuilder withStatus(ApplicationUser.UserStatus status) {
            this.status = status;
            return this;
        }

        public UserDtoBuilder withRole(ApplicationUser.UserRole role) {
            this.role = role;
            return this;
        }

        public UserDtoBuilder withAddressDto(AddressDto addressDto) {
            this.addressDto = addressDto;
            return this;
        }

        public UserDto build() {
            UserDto userDto = new UserDto();
            userDto.setId(id);
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setTelephoneNumber(telephoneNumber);
            userDto.setEmail(email);
            userDto.setPassword(password);
            userDto.setLastLogin(lastLogin);
            userDto.setPoints(points);
            userDto.setStatus(status);
            userDto.setRole(role);
            userDto.addressDto = this.addressDto;
            return userDto;
        }
    }
}
