/**
 * CategoryDto.java
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
 * Data Transfer Object (DTO) representing a category in the application.
 * The CategoryDto class is used to transfer category-related data between different layers of the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    @NotEmpty(message = "Title should not be empty")
    @Size(max = 32, message = "category title should not be more than 32 characters")
    private String title;
    private String categoryExplanation;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    private List<ItemDto> items;

    public CategoryDto(String title, String explanation) {
        this.title = title;
        this.categoryExplanation = explanation;
    }
    public CategoryDto(String title) {
        this.title = title;
    }
}

