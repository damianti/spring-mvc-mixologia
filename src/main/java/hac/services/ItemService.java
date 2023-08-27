/** ItemService.java:*/
package hac.services;
import hac.dto.ItemDto;
import hac.models.Item;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<ItemDto> findAllItems();
    ItemDto createItem(ItemDto itemDto);
    ItemDto updateItem(ItemDto itemDto);
    List<ItemDto> searchItems(String query);
    void deleteItem(Long itemId);
    List<ItemDto> findItemsByCategoryId(Long categoryId);
    Optional<ItemDto> findItemById(Long itemId);

    Optional<ItemDto> findItemByTitle(String itemId);
    ItemDto convertToDto(Item item);

    ItemDto findItemByReviewId(Long reviewId);
}
