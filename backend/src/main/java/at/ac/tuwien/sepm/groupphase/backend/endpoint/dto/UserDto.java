package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private AddressDto address;
}
