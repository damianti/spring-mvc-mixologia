/** ReviewServiceImpl.java: */
package hac.services.impl;

import hac.dto.ItemDto;
import hac.dto.ReviewDto;
import hac.dto.UserDto;
import hac.models.Review;
import hac.models.Item;
import hac.models.User;
import hac.repositories.ReviewRepository;
import hac.services.ReviewService;
import hac.services.ItemService;
import hac.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * ReviewServiceImpl is an implementation of the ReviewService interface
 * that provides methods to perform various operations related to reviews.
 * and is the service in charge of the ReviewRepository
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private ItemService itemService;
    private final UserService userService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,  UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }
    @Autowired
    public void setItemService(@Lazy ItemService itemService) {
        this.itemService = itemService;
    }
    @Override
    public List<ReviewDto> findAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = convertToEntity(reviewDto);
        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }

    @Override
    public ReviewDto getReview(Long reviewId) {
        Optional <Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            return convertToDto(reviewOptional.get());
        } else {
            throw new RuntimeException("Item not found for review id: " + reviewId);
        }
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));
        reviewRepository.deleteById(reviewId);
    }


    private Review convertToEntity(ReviewDto reviewDto) {
        Review review = new Review();
        BeanUtils.copyProperties(reviewDto, review);

        Optional<ItemDto> itemDto = itemService.findItemById(reviewDto.getItemId());
        Optional<UserDto> userDto = userService.findUserById(reviewDto.getUserId());

        itemDto.ifPresent(dto -> review.setItem(convertToEntity(dto)));
        userDto.ifPresent(dto -> review.setUser(convertToEntity(dto)));

        return review;
    }

    public ReviewDto convertToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        BeanUtils.copyProperties(review, reviewDto);
        reviewDto.setUserId(review.getUser().getId());
        reviewDto.setUsername(review.getUser().getUsername());
        reviewDto.setItemId(review.getItem().getId());
        UserDto authenticatedUser = userService.getAuthenticatedUser();
        if(authenticatedUser != null) {
            boolean ownsReview = Objects.equals(review.getUser().getEmail(), authenticatedUser.getEmail());
            boolean isAdmin = authenticatedUser.getRole().contains("ADMIN");
            reviewDto.setCanDelete(ownsReview || isAdmin);
        } else {
            reviewDto.setCanDelete(false);
        }


        return reviewDto;
    }
    private Item convertToEntity(ItemDto itemDto) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDto, item);
        return item;
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }
}
