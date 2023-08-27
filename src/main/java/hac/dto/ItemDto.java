/**
 * ItemDto.java
 *
 */
package hac.dto;

//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Data Transfer Object (DTO) representing an item in the application.
 * The ItemDto class is used to transfer item-related data between different layers of the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotEmpty(message = "Title should not be empty")
    @Size(max = 32, message = "item title should not be more than 32 characters")
    private String title;
//    @Pattern(regexp = "(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)",
//            message = "Invalid image URL, should end with .jpg, .gif, .png etc.")
    @NotEmpty(message = "Photo link should not be empty")
    private String photoUrl;

    /**
     * The content or description of the item.
     * It should not be empty, should have a minimum length of 40 characters, and should not exceed 300 characters.
     */
    @NotEmpty(message = "Content should not be empty")
    @Size(max = 300, message = "content should not be more than 300 characters")
    @Size(min = 40, message = "content should not be less than 40 characters")
    private String content;
    @NotEmpty(message = "category title is mandatory")
    @Size(max = 32, message = "category title should not be more than 32 characters")
    private String categoryTitle;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private List<ReviewDto> reviews;
    @Builder.Default
    private Integer ratingAvg = 2;

    public ItemDto(String title, String photoUrl, String content, String categoryTitle) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.content = content;
        this.categoryTitle = categoryTitle;
        this.ratingAvg = 2; //starting default value with 2 from 10

    }

    /**
     * Calculates the average rating of the item based on the associated reviews.
     * The calculated average rating is stored in the 'ratingAvg' field.
     */
    public void calculateRatingAvg() {
        double sum = 0.0;
        int count = 0;
        for (ReviewDto review : reviews) {
            sum += review.getRating();
            count++;
        }
        double average = count > 0 ? sum / count : 0.0;
        double doubledAverage = 2 * average;
        ratingAvg = (int) Math.round(doubledAverage);
    }

}


