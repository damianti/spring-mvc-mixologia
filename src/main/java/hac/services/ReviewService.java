/** ReviewService.java:*/
package hac.services;
import hac.dto.ReviewDto;
import hac.models.Review;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> findAllReviews();
    ReviewDto createReview(ReviewDto reviewDto);

    ReviewDto getReview(Long reviewId);
    void deleteReview(Long reviewId);
    ReviewDto convertToDto(Review review);

}
