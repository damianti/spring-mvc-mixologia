/**
 * ReviewDto.java
 *
 */
package hac.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a review in the application.
 * The ReviewDto class is used to transfer review-related data between different layers of the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    @NotEmpty(message = "Comment is mandatory")
    @Size(max = 400, message = "content should not be more than 300 characters")
    private String comment;

    @Min(value = 1,  message = "Rating must be 1 or greater")
    @Max(value = 5, message = "Rating must be 5 or less")
    @NotNull(message = "rating is mandatory")
    @Builder.Default
    private Integer rating = 1;

    private Long userId;
    private String username;
    private Long itemId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Boolean canDelete;

    public ReviewDto(String comment, Integer rating, Long userId, Long itemId, String username){
        this.comment = comment;
        this.rating = rating;
        this.userId = userId;
        this.itemId = itemId;
        this.username = username;
        this.canDelete = false;
    }
}

