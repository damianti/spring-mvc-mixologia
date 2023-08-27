/**
 * ReviewRepository.java
 *
 */
package hac.repositories;

import hac.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long reviewId);
}
