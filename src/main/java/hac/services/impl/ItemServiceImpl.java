/** ItemServiceImpl.java: */
package hac.services.impl;

import hac.dto.CategoryDto;
import hac.dto.ItemDto;
import hac.dto.ReviewDto;
import hac.models.Category;
import hac.models.Item;
import hac.repositories.ItemRepository;
import hac.services.CategoryService;
import hac.services.ItemService;
import hac.services.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * ItemServiceImpl is an implementation of the ItemService interface
 * that provides methods to perform various operations related to items
 * and is the service in charge of the ItemRepository
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    private final CategoryService categoryService;

    private final ReviewService reviewService;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, CategoryService categoryService,ReviewService reviewService) {
        this.itemRepository = itemRepository;
        this.categoryService = categoryService;
        this.reviewService = reviewService;

    }

    @Override
    public List<ItemDto> findAllItems() {
        List<Item> items = itemRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    @Override
    public ItemDto createItem(ItemDto itemDto) {
        // Check if the category exists, if not, create it
        Optional<CategoryDto> categoryDto = categoryService.findCategoryByTitle(itemDto.getCategoryTitle());
        if(categoryDto.isEmpty()) {
            CategoryDto newCategoryDto = new CategoryDto(itemDto.getCategoryTitle());
            categoryService.createCategory(newCategoryDto);
        }

        Item item = convertToEntity(itemDto);
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }
    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + itemDto.getId()));
        item.setTitle(itemDto.getTitle());
        item.setContent(itemDto.getContent());
        item.setPhotoUrl(itemDto.getPhotoUrl());

        Optional<CategoryDto> categoryDto = categoryService.findCategoryByTitle(itemDto.getCategoryTitle());
        Category category;
        if (categoryDto.isPresent()) {
            category = convertToEntity(categoryDto.get());
        } else {

            CategoryDto newCategoryDto = new CategoryDto(itemDto.getCategoryTitle());
            category = convertToEntity(categoryService.createCategory(newCategoryDto));
        }
        item.setCategory(category);

        item = itemRepository.save(item);
        return convertToDto(item);
    }

    @Override
    public List<ItemDto> searchItems(String query) {
        List<Item> searchResults = itemRepository.findByTitleContainingIgnoreCase(query);
        return searchResults.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<ItemDto> findItemById(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.map(this::convertToDto);
    }


    @Transactional
    @Override
    public Optional<ItemDto> findItemByTitle(String itemTitle) {
        Optional<Item> item = itemRepository.findByTitle(itemTitle);
        return item.map(this::convertToDto);
    }



    @Override
    public ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        BeanUtils.copyProperties(item, itemDto);

        if (item.getCategory() != null) {
            itemDto.setCategoryTitle(item.getCategory().getTitle());
        }

        List<ReviewDto> reviewDtos = item.getReviews().stream()
                .map(reviewService::convertToDto)
                .collect(Collectors.toList());

        itemDto.setReviews(reviewDtos);
        itemDto.calculateRatingAvg();

        return itemDto;
    }




    private Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        return category;
    }


    private Item convertToEntity(ItemDto itemDto) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDto, item);
        Optional<CategoryDto> categoryDto = categoryService.findCategoryByTitle(itemDto.getCategoryTitle());

        categoryDto.ifPresent(dto -> item.setCategory(convertToEntity(dto)));
        return item;
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + itemId));
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> findItemsByCategoryId(Long categoryId) {
        List<Item> items = itemRepository.findAllByCategoryId(categoryId);

        return items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findItemByReviewId(Long reviewId) {
        Optional<Item> itemOptional = itemRepository.findByReviews_Id(reviewId);
        if (itemOptional.isPresent()) {
            return convertToDto(itemOptional.get());
        } else {
            throw new RuntimeException("Item not found for review id: " + reviewId);
        }
    }

}
