/**
    CategoryController.java:
 The CategoryController class is a controller class that handles requests related to categories.
 */
package hac.controllers;

import hac.dto.CategoryDto;
import hac.dto.ItemDto;
import hac.services.CategoryService;
import hac.services.ItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    public CategoryController(CategoryService categoryService, ItemService itemService) {

        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    /**
     * Handles the GET request to list all categories.
     *
     * @param model the Model object to add attributes to
     * @return the view name for the categories list page
     */
    @GetMapping
    public String listCategories(Model model) {
        addCommonAttributes(model);
        return "categories-list";
    }

    /**
     * Handles the GET request to view a specific category.
     *
     * @param categoryId the ID of the category to view
     * @param model      the Model object to add attributes to
     * @return the view name for the category view page
     * @throws NoHandlerFoundException if the category is not found
     */
    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    public String getCategory(@PathVariable("categoryId") Long categoryId, Model model) throws NoHandlerFoundException {
        addCommonAttributes(model);
        try {
            Optional<CategoryDto> categoryDto = categoryService.findCategoryById(categoryId);
            if (categoryDto.isPresent()) {
                List<ItemDto> itemsOfCategory = itemService.findItemsByCategoryId(categoryDto.get().getId());
                model.addAttribute("category", categoryDto.get());
                model.addAttribute("items", itemsOfCategory);
                return "category-view";
            } else {
                // no item is found with this ID
                System.out.println("No category found with ID: " + categoryId);
                logger.warn("No category found with ID: {}", categoryId);
                throw new NoHandlerFoundException("GET", "/" + categoryId, null);
            }
        } catch (Exception e) {
            // If there's an error, log it and return the error page
            logger.error("Exception when fetching item with ID: {}", categoryId, e);
            System.out.println("Exception when fetching item with error: " + e);
            throw new NoHandlerFoundException("GET", "/" + categoryId, null);
        }
    }


    /* Admin Section for categories management */

    /**
     * Handles the GET request to display the form for adding a new category (admin only).
     *
     * @param model the Model object to add attributes to
     * @return the view name for the add category form
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("admin/add")
    public String addCategoryForm(Model model) {
        addCommonAttributes(model);
        model.addAttribute("category", new CategoryDto());
        return "admin/add-category";
    }

    /**
     * Handles the POST request to add a new category (admin only).
     *
     * @param categoryDto the CategoryDto object containing the category data
     * @param result      the BindingResult object for validation errors
     * @param model       the Model object to add attributes to
     * @return a redirect to the category view page if successful, or the add category form if there are errors
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("admin/add")
    public String handleCategoryForm(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addCommonAttributes(model);
            model.addAttribute("category", categoryDto);
            model.addAttribute("errors", result.getAllErrors());
            return "admin/add-category";
        }
        if (categoryService.findCategoryByTitle(categoryDto.getTitle()).isPresent() ){
            addCommonAttributes(model);
            logger.warn("adding category title: {} already in use", categoryDto.getTitle());
            model.addAttribute("category", categoryDto);
            model.addAttribute("titleExists", true);
            return "admin/add-category";
        }
        logger.info("Received item: {}", categoryDto);
        System.out.println("Received item: " + categoryDto);
        CategoryDto category = categoryService.createCategory(categoryDto);
        return "redirect:/category/" + category.getId();
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }

    /**
     * Handles the GET request to display the form for editing a category (admin only).
     *
     * @param categoryId the ID of the category to edit
     * @param model      the Model object to add attributes to
     * @return the view name for the edit category form
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("admin/edit/{categoryId}")
    public String editCategoryForm(@PathVariable("categoryId") Long categoryId, Model model) {
        addCommonAttributes(model);
        Optional<CategoryDto> categoryDto = categoryService.findCategoryById(categoryId);
        if (categoryDto.isPresent()) {
            model.addAttribute("category", categoryDto.get());
            return "admin/edit-category";  // edit-item should be a Thymeleaf template to show the form
        } else {
            // handle case where no item is found with this ID
            logger.warn("No category found with ID: {}", categoryId);
            return "error"; // the name of the Thymeleaf template that shows an error
        }
    }

    /**
     * Handles the POST request to edit a category (admin only).
     *
     * @param categoryDto the CategoryDto object containing the updated category data
     * @param result      the BindingResult object for validation errors
     * @param model       the Model object to add attributes to
     * @return a redirect to the category view page if successful, or the edit category form if there are errors
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("admin/edit")
    public String handleCategoryEditForm(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("category", categoryDto);
            model.addAttribute("errors", result.getAllErrors());
            return "admin/edit-category";
        }
        if (categoryService.findCategoryByTitle(categoryDto.getTitle()).isPresent() ){
            logger.warn("editing category title: {} already in use", categoryDto.getTitle());
            model.addAttribute("category", categoryDto);
            model.addAttribute("titleExists", true);
            return "admin/edit-category";
        }
        logger.info("Received updated category: {}", categoryDto);
        CategoryDto category = categoryService.updateCategory(categoryDto);
        return "redirect:/category/" + category.getId();
    }

    /**
     * Handles the DELETE request to delete a category (admin only).
     *
     * @param categoryId the ID of the category to delete
     * @return a redirect to the categories list page
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        try {
            logger.info("Delete category id: {}", categoryId);
            categoryService.deleteCategory(categoryId);
            return "redirect:/category"; // Redirects to the users list page after successful deletion
        } catch (Exception e) {
            logger.error("Exception when trying to delete user with ID: {}", categoryId, e);
            return "error";
        }
    }


}
