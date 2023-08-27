/**
 * User.java
 *
 */
package hac.models;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;



import java.io.Serializable;
/**
 * Represents a user in the application.
 * The User class is an entity class mapped to the "users" table in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Mail is mandatory")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Name is mandatory")
    @Size(max = 32, message = "Username should not be more than 32 characters")
    private String username;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;


    @NotEmpty(message = "Role is mandatory")
    @Builder.Default
    private String role = "USER";
    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();
    public User(String email, String username, String password, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + username + ", email=" + email + '}';
    }
}

