/**
 * UserDto.java
 *
 */
package hac.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a user in the application.
 * The UserDto class is used to transfer user-related data between different layers of the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "email should not be empty")
    private String email;
    @NotEmpty(message = "username should not be empty")
    @Size(max = 32, message = "Username should not be more than 32 characters")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;
    @NotEmpty(message = "role should not be empty")
    @Builder.Default
    private String role = "USER";

    @NotEmpty(message = "Confirm Password should not be empty")
    @Size(min = 6, message = "Confirm Password should be at least 6 characters")
    private String confirmPassword;
    private List<ReviewDto> reviews;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public UserDto(String email, String username, String password, String confirmPassword, String role){
        this.email = email;
        this.username = username;
        this.confirmPassword = confirmPassword;
        this.password = password;
        this.role = role;
    }
}
