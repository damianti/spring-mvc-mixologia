/** CategoryService.java:*/
package hac.services;

import hac.dto.CategoryDto;
import hac.models.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> findAllCategories();
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

//    List<CategoryDto> searchCategories(String query);
    void deleteCategory(Long id);
    Optional<CategoryDto> findCategoryById(Long categoryId);
    Optional<CategoryDto> findCategoryByTitle(String categoryId);
    CategoryDto convertToDto(Category category);

}

