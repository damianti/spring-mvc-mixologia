/** CategoryServiceImpl.java: */
package hac.services.impl;

import hac.dto.CategoryDto;
import hac.dto.ItemDto;
import hac.models.Category;
import hac.models.Item;
import hac.repositories.CategoryRepository;
import hac.services.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CategoryServiceImpl is an implementation of the CategoryService interface
 * that provides methods to perform various operations related to categories.
 * and is the service in charge of the CategoryRepository
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<CategoryDto> findAllCategories() {
        List<Category> items = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Optional<Category> category = categoryRepository.findById(categoryDto.getId());

        if (category.isEmpty()) {
            throw new RuntimeException("Category with id " + categoryDto.getId() + " does not exist");
        }

        Category categoryEntity = category.get();
        categoryEntity.setTitle(categoryDto.getTitle());
        categoryEntity.setCategoryExplanation(categoryDto.getCategoryExplanation());
        Category updatedCategory = categoryRepository.save(categoryEntity);

        return convertToDto(updatedCategory);
    }


//    @Override
//    public List<CategoryDto> searchCategories(String query) {
//        List<Category> searchResults = categoryRepository.findByTitleContainingIgnoreCase(query);
//        return searchResults.stream().map(this::convertToDto).collect(Collectors.toList());
//    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Optional<CategoryDto> findCategoryById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(this::convertToDto);
    }
    @Transactional
    @Override
    public Optional<CategoryDto> findCategoryByTitle(String categoryTitle) {
        Optional<Category> category = categoryRepository.findByTitle(categoryTitle);
        return category.map(this::convertToDto);
    }


    @Override
    public CategoryDto convertToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto);

        List<ItemDto> itemDtos = category.getItems().stream()
                .map(this::convertItemToDto)
                .collect(Collectors.toList());

        categoryDto.setItems(itemDtos);
        return categoryDto;
    }

    private ItemDto convertItemToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        BeanUtils.copyProperties(item, itemDto);
        return itemDto;
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        return category;
    }

}

