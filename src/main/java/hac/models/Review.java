/**
 * Review.java
 *
 */
package hac.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;


/**
 * Represents a review for an item in the application.
 * The Review class is an entity class mapped to the "reviews" table in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
public class Review implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Comment is mandatory")
    private String comment;

    @Min(value = 1,  message = "Rating must be 1 or greater")
    @Max(value = 5, message = "Rating must be 5 or less")
    @NotNull(message = "rating is mandatory")
    private Integer rating;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;




}

