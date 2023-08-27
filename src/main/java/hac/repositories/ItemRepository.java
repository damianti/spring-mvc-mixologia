/**
 * ItemRepository.java
 *
 */
package hac.repositories;

import hac.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Finds an item by its title.
     *
     * @param title The title of the item.
     * @return An Optional containing the found item, or an empty Optional if not found.
     */
    Optional<Item> findByTitle(String title);

    /**
     * Finds items whose title contains the given query (case-insensitive).
     *
     * @param query The query to search for in item titles.
     * @return A list of items matching the query.
     */
    List<Item> findByTitleContainingIgnoreCase(String query);

    /**
     * Finds all items belonging to a specific category.
     *
     * @param categoryId The ID of the category.
     * @return A list of items belonging to the category.
     */
    List<Item> findAllByCategoryId(Long categoryId);

    /**
     * Finds an item by the ID of one of its reviews.
     *
     * @param reviewId The ID of the review.
     * @return An Optional containing the found item, or an empty Optional if not found.
     */
    Optional<Item> findByReviews_Id(Long reviewId);
}
