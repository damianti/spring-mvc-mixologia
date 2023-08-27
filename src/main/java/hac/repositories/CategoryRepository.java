/**
 * CategoryRepository.java
 *
 */
package hac.repositories;
import hac.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds a category by its title.
     *
     * @param title The title of the category.
     * @return An Optional containing the found category, or an empty Optional if not found.
     */
    Optional<Category> findByTitle(String title);
    /**
     * Finds categories whose title contains the given query (case-insensitive).
     *
     * @param query The query to search for in category titles.
     * @return A list of categories matching the query.
     */
    List<Category> findByTitleContainingIgnoreCase(String query);
}
