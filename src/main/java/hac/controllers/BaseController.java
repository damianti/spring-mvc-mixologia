/**
    BaseController.java
 The BaseController class is an abstract controller class that provides common functionality and attributes for other controllers.
 */
package hac.controllers;

import hac.dto.CategoryDto;
import hac.dto.UserDto;
import hac.services.CategoryService;
import hac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;

public abstract class BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;
    /**
     * Adds common attributes to the model, such as the authenticated user's username and the list of categories.
     *
     * @param model the Model object to add attributes to
     */
    protected void addCommonAttributes(Model model) {
        try {
            UserDto user = userService.getAuthenticatedUser();
            if (user != null) {
                model.addAttribute("username", user.getUsername());
            }

            List<CategoryDto> categories = categoryService.findAllCategories();
            if (!categories.isEmpty()) {
                model.addAttribute("categories", categories);
            } else {
                model.addAttribute("noCategoriesMessage", "No categories found.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error retrieving user name and category items, try later.");
        }
    }
}

