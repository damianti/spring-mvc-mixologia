/**
    PublicController.java:
 The PublicController class is a controller class that handles requests related to public access urls and user-related operations..
 */
package hac.controllers;

import hac.dto.ItemDto;
import hac.dto.UserDto;
import hac.services.ItemService;
import hac.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PublicController extends BaseController {
        private final ItemService itemService;
        private final UserService userService;


    @Autowired
    public PublicController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    /** Home page. */
    /**
     * Handles the request for the home page.
     * Retrieves a list of items from the itemService and adds it to the model for display on the home page.
     * @param model the model object for the view
     * @return the name of the home page template
     */
    @RequestMapping("/")
    public String index(Model model) {
        List<ItemDto> items = itemService.findAllItems();
        items.forEach(item -> System.out.println("Item: " + item.getTitle() + " Reviews: " + item.getReviews()));
        if (!items.isEmpty()) {
            model.addAttribute("items", items);
        } else{
            model.addAttribute("noItemsMessage", "No items found.");
        }

        model.addAttribute("currentPage", "home");
        addCommonAttributes(model);
        return "index";
    }

    /**
     * Handles the request for the "About Us" page.
     * Adds common attributes to the model and returns the name of the "about-us" template.
     * @param model the model object for the view
     * @return the name of the "about-us" template
     */
    @GetMapping("/about-us")
    //try category noly
    public String aboutUs(Model model) {
        addCommonAttributes(model);
        model.addAttribute("currentPage", "about-us");
        return "about-us"; // return your template name
    }

    /**
     * Handles the request for the "Search Drinks" page.
     * Adds common attributes to the model and returns the name of the "search-drinks" template.
     * @param model the model object for the view
     * @return the name of the "search-drinks" template
     */
    @GetMapping("/search")
    public String searchDrinks(Model model) {
        addCommonAttributes(model);
        model.addAttribute("currentPage", "search");
        return "search"; // return your template name
    }

    /**
     * Handles the request for the login page.
     * Adds common attributes to the model and returns the name of the "login" template.
     * @param model the model object for the view
     * @return the name of the "login" template
     */
    @GetMapping("/login")
    public String login(Model model) {
        addCommonAttributes(model);
        return "login";
    }

    /**
     * Handles the request for the registration page.
     * Adds common attributes to the model, creates a new UserDto object, and returns the name of the "register" template.
     * @param model the model object for the view
     * @return the name of the "register" template
     */
    @GetMapping("/register")
    public String register(Model model) {
        addCommonAttributes(model);
        model.addAttribute("user", new UserDto());
        return "register";
    }

    /**
     * Handles the submission of the registration form.
     * Validates the UserDto object, creates a new user with the provided data, and redirects the user to the login page.
     * @param userDto the UserDto object containing the user's registration data
     * @param result the binding result for validating the form
     * @param model the model object for the view
     * @return a redirect to the login page
     */
    @PostMapping("/register")
    public String handleUserForm(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("errors in register: " + result.getAllErrors());
            model.addAttribute("user", userDto);
            model.addAttribute("errors", result.getAllErrors());
            return "register";
        }
        if (userService.findUserByEmail(userDto.getEmail()).isPresent() ){
            System.out.println("errors in register: email already in use");
            model.addAttribute("user", userDto);
            model.addAttribute("emailExists", true);
            return "register";
        }
        System.out.println("Received user: " + userDto);
        userDto.setRole("USER");
        UserDto user = userService.createUser(userDto);
        return "redirect:/login";
    }

    /**
     * Handles the request for the user zone index page.
     * @return the name of the user index page template
     */
    @RequestMapping("/user")
    public String userIndex() {
        return "user/index";
    }

}