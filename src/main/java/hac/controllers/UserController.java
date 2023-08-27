package hac.controllers;


import hac.dto.UserDto;
import hac.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    public UserController( UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the list of all users.
     * @param model the model object for the view
     * @return the view name for the users list page
     */
    @Secured("ROLE_ADMIN")
    @GetMapping
    public String listUsers(Model model) {
        try {
            addCommonAttributes(model);
            List<UserDto> users = userService.getAllUsers();
            if (!users.isEmpty()) {
                model.addAttribute("users", users);
            } else{
                model.addAttribute("noUsersMessage", "No users found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving items, try later.");
        }
        return "users-list";
    }

    /**
     * Displays the form for adding a new user.
     * @param model the model object for the view
     * @return the view name for the add user form
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("admin/add")
    public String addUserForm(Model model) {
        addCommonAttributes(model);
        UserDto user = userService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }
        model.addAttribute("user", new UserDto());
        return "admin/add-user";
    }

    /**
     * Handles the submission of the add user form.
     * Validates the UserDto object and creates a new user.
     * @param userDto the UserDto object containing the user data
     * @param result the binding result for validating the form
     * @param model the model object for the view
     * @return a redirect to the users list page after successful creation, or the add user form with errors
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("admin/add")
    public String handleUserForm(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            model.addAttribute("errors", result.getAllErrors());
            return "admin/add-user";
        }
        if (userService.findUserByEmail(userDto.getEmail()).isPresent() ){
            logger.warn("email: {} already in use", userDto.getEmail());
            model.addAttribute("user", userDto);
            model.addAttribute("emailExists", true);
            return "admin/add-user";
        }
        logger.info("Received user: {}", userDto);
        System.out.println("Received user: " + userDto);
        UserDto user = userService.createUser(userDto);
        return "redirect:/users";
    }

    /**
     * Handles the request for editing a user.
     * Displays the edit user form pre-populated with the user's data.
     * @param userId the ID of the user to edit
     * @param model the model object for the view
     * @return the view name for the edit user form, or an error page if the user is not found
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("admin/edit/{userId}")
    public String editUserForm(@PathVariable("userId") Long userId, Model model) {
        addCommonAttributes(model);
        Optional<UserDto> userDto = userService.findUserById(userId);
        if (userDto.isPresent()) {
            model.addAttribute("user", userDto.get());
            return "admin/edit-user";
        } else {
            // handle case where no item is found with this ID
            logger.warn("No user found with ID: {}", userId);
            return "error";
        }
    }

    /**
     * Handles the submission of the user edit form.
     * Validates the UserDto object and updates the user.
     * @param userDto the UserDto object containing the updated user data
     * @param result the binding result for validating the form
     * @param model the model object for the view
     * @return a redirect to the users list page after successful update, or the user edit form if there are errors
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("admin/edit")
        public String handleUserEditForm(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            model.addAttribute("errors", result.getAllErrors());
            return "admin/edit-user";
        }
        if (userService.findUserByEmail(userDto.getEmail()).isPresent() ){
            logger.warn("email: {} already in use", userDto.getEmail());
            model.addAttribute("user", userDto);
            model.addAttribute("emailExists", true);
            return "admin/edit-user";
        }
        logger.info("Received updated user: {}", userDto);
        UserDto user = userService.updateUser(userDto);
        return "redirect:/users";
    }

    /**
     * Deletes a user by ID.
     * @param id the ID of the user to delete
     * @return a redirect to the users list page after successful deletion, or an error page if there's an issue
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            logger.info("Delete user id: {}", id);
            userService.deleteUser(id);
            return "redirect:/users"; // Redirects to the users list page after successful deletion
        } catch (Exception e) {
            logger.error("Exception when trying to delete user with ID: {}", id, e);
            return "error";
        }
    }


}