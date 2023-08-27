/**
 * ReviewController.java:
 * Controller class for handling reviews.
 */
package hac.controllers;

import hac.dto.ItemDto;
import hac.dto.ReviewDto;
import hac.dto.UserDto;
import hac.services.ItemService;
import hac.services.ReviewService;
import hac.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService, ItemService itemService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.itemService = itemService;
    }



    /**
     * Retrieves all reviews.
     * @return a list of all reviews
     */
    @GetMapping
    public List<ReviewDto> getAllReviews() {
        return reviewService.findAllReviews();
    }

    /**
     * Handles the request for adding a review to an item.
     * Validates the ReviewDto object, retrieves the authenticated user, and associates the review with the item and user.
     * @param itemId the ID of the item to add the review to
     * @param reviewDto the ReviewDto object containing the review data
     * @param result the binding result for validating the form
     * @param model the model object for the view
     * @return a redirect to the item page
     */
    @Secured({"ROLE_ADMIN", "USER"})
    @PostMapping("/items/{itemId}")
    public String addReview(@PathVariable Long itemId, @Valid @ModelAttribute("review") ReviewDto reviewDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("review", reviewDto);
            model.addAttribute("errors", result.getAllErrors());
            return "/items/" + itemId;
        }
        logger.info("Received a request to add a review for item with ID: {}", reviewDto);

        UserDto userDto = userService.getAuthenticatedUser();
        Optional<ItemDto> itemDto = itemService.findItemById(itemId);
        if (itemDto.isEmpty()) {
            result.rejectValue("itemId", "error.itemId", "Item not found");
            model.addAttribute("review", reviewDto);
            model.addAttribute("errors", result.getAllErrors());
            return "/items/" + itemId;
        }
        if (userDto == null) {
            result.rejectValue("userId", "error.userId", "User not found");
            model.addAttribute("review", reviewDto);
            model.addAttribute("errors", result.getAllErrors());
            return "/items/" + itemId;
        }

        if (!itemId.equals(reviewDto.getItemId())) {
            result.rejectValue("itemId", "error.itemId", "Mismatched Item ID");
            model.addAttribute("review", reviewDto);
            model.addAttribute("errors", result.getAllErrors());
            return "items/" + itemId;
        }
        reviewDto.setUserId(userDto.getId());
        reviewDto.setUsername(userDto.getUsername());
        logger.info("Received a request to add a review for item with ID: {}", reviewDto.getUsername());
        ReviewDto review = reviewService.createReview(reviewDto);
        return "redirect:/items/" + itemId;
    }

    /**
     * Handles the request for deleting a review.
     * Validates the user's permission, item, and review before deleting the review.
     * @param reviewId the ID of the review to delete
     * @param model the model object for the view
     * @return a redirect to the item page after successful deletion, or an error page if there's an issue
     */
    @DeleteMapping("/delete/{reviewId}")
    public String deleteCategory(@PathVariable Long reviewId, Model model) {
        try {
            UserDto userDto = userService.getAuthenticatedUser();
            ItemDto itemDto = itemService.findItemByReviewId(reviewId);
            if (itemDto == null) {
                model.addAttribute("error", "Item not found");
                return "error"; // return to a specific error page
            }

            if (userDto == null) {
                model.addAttribute("error", "User not found");
                return "error"; // return to a specific error page
            }
            ReviewDto reviewDto = reviewService.getReview(reviewId);
            if (reviewDto == null || !itemDto.getId().equals(reviewDto.getItemId())) {
                model.addAttribute("error", "Mismatched Item ID or Review not found");
                return "error"; // return to a specific error page
            }
            if (!userDto.getId().equals(reviewDto.getUserId()) && !userDto.getRole().equals("ADMIN")) {
                model.addAttribute("error", "You don't have permission to delete this review");
                return "error"; // return to a specific error page
            }
            logger.info("Delete review id: {}", reviewId);
            reviewService.deleteReview(reviewId);
            return "redirect:/items/" + itemDto.getId();
        } catch (Exception e) {
            logger.error("Exception when trying to delete review with ID: {}", reviewId, e);
            return "error"; // return to a specific error page
        }

    }

}
