/**
    ItemController.java:
 The ItemController class is a controller class that handles requests related to items.
 */
package hac.controllers;


import hac.dto.ItemDto;
import hac.dto.ReviewDto;
import hac.services.ItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/items")
public class ItemController extends BaseController {
    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Handles the GET request to display the list of items.
     *
     * @param model the Model object to add attributes to
     * @return the view name for the items list page
     */
    @GetMapping
    public String listItems(Model model) {
        try {
            addCommonAttributes(model);
            List<ItemDto> items = itemService.findAllItems();
            if (!items.isEmpty()) {
                model.addAttribute("items", items);
            } else{
                    model.addAttribute("noItemsMessage", "No items found.");
                }
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Error retrieving items, try later.");
            }
        return "items-list";
    }

    /**
     * Handles the GET request to display an item.
     *
     * @param itemId the ID of the item to display
     * @param model  the Model object to add attributes to
     * @return the view name for the item view page
     * @throws NoHandlerFoundException if no item is found with the specified ID
     */
    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public String getItem(@PathVariable("itemId") Long itemId, Model model) throws NoHandlerFoundException {
        try {
            addCommonAttributes(model);
            Optional<ItemDto> itemDto = itemService.findItemById(itemId);
            if (itemDto.isPresent()) {
                model.addAttribute("item", itemDto.get());
                model.addAttribute("review", new ReviewDto());

                return "item-view";  // the name of the Thymeleaf template that shows the item
            } else {
                // handle case where no item is found with this ID
                System.out.println("No item found with ID: " + itemId);
                logger.warn("No item found with ID: {}", itemId);
                throw new NoHandlerFoundException("GET", "/" + itemId, null);
            }
        } catch (Exception e) {
            // If there's an error, log it and return the error page
            logger.error("Exception when fetching item with ID: {}", itemId, e);
            System.out.println("Exception when fetching item with error: " + e);
            throw new NoHandlerFoundException("GET", "/" + itemId, null);
        }
    }


    /**
     * Handles the GET request to search for items.
     *
     * @param query the search query
     * @param model the Model object to add attributes to
     * @return the view name for the items list page with search results
     */
    @GetMapping("/search")
    public String searchItems(@RequestParam("query") String query, Model model) {
        try {
            addCommonAttributes(model);
            List<ItemDto> searchResults = itemService.searchItems(query);

            if (!searchResults.isEmpty()) {
                model.addAttribute("items", searchResults);
            }
            else{
                model.addAttribute("noItemsMessage", "No items found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving items, try later.");
        }
        return "items-list";
    }

    /* Admin Section for items management */

    /**
     * Handles the GET request to display the add item form.
     *
     * @param model the Model object to add attributes to
     * @return the view name for the add item form
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("admin/add")
    public String addItemForm(Model model) {
        addCommonAttributes(model);
        model.addAttribute("item", new ItemDto());
        return "admin/add-item";
    }


    /**
     * Handles the POST request to handle the add item form submission.
     *
     * @param itemDto the ItemDto object containing the item data
     * @param result  the BindingResult object for validating the form data
     * @param model   the Model object to add attributes to
     * @return the view name for the added item view page
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("admin/add")
    public String handleItemForm(@Valid @ModelAttribute("item") ItemDto itemDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addCommonAttributes(model);
            model.addAttribute("item", itemDto);
            model.addAttribute("errors", result.getAllErrors());
            return "admin/add-item";
        }
        if (itemService.findItemByTitle(itemDto.getTitle()).isPresent() ){
            addCommonAttributes(model);
            logger.warn("adding item title: {} already in use", itemDto.getTitle());
            model.addAttribute("item", itemDto);
            model.addAttribute("titleExists", true);
            return "admin/add-item";
        }
        logger.info("Received item: {}", itemDto);
        ItemDto item = itemService.createItem(itemDto);
        return "redirect:/items/" + item.getId();
    }

    /**
     * Handles the GET request to display the edit item form.
     *
     * @param itemId the ID of the item to edit
     * @param model  the Model object to add attributes to
     * @return the view name for the edit item form
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("admin/edit/{itemId}")
    public String editItemForm(@PathVariable("itemId") Long itemId, Model model) {
        addCommonAttributes(model);
        Optional<ItemDto> itemDto = itemService.findItemById(itemId);
        if (itemDto.isPresent()) {
            model.addAttribute("item", itemDto.get());
            return "admin/edit-item";
        } else {

            logger.warn("No item found with ID: {}", itemId);
            return "error";
        }
    }

    /**
     * Handles the POST request to handle the edit item form submission.
     *
     * @param itemDto the ItemDto object containing the updated item data
     * @param result  the BindingResult object for validating the form data
     * @param model   the Model object to add attributes to
     * @return the view name for the updated item view page
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("admin/edit")
    public String handleEditItemForm(@Valid @ModelAttribute("item") ItemDto itemDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addCommonAttributes(model);
            model.addAttribute("item", itemDto);
            model.addAttribute("errors", result.getAllErrors());
            return "admin/edit-item";
        }
        if (itemService.findItemByTitle(itemDto.getTitle()).isPresent() ){
            addCommonAttributes(model);
            logger.warn("editing item title: {} already in use", itemDto.getTitle());
            model.addAttribute("item", itemDto);
            model.addAttribute("titleExists", true);
            return "admin/edit-item";
        }
        logger.info("Received updated item: {}", itemDto);
        ItemDto item = itemService.updateItem(itemDto);
        return "redirect:/items/" + item.getId();
    }

    /**
     * Handles the DELETE request to delete an item.
     *
     * @param itemId the ID of the item to delete
     * @return the view name for the items list page after successful deletion
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long itemId) {
        try {
            logger.info("Delete item id: {}", itemId);
            itemService.deleteItem(itemId);
            return "redirect:/items"; // Redirects to the item list page after successful deletion
        } catch (Exception e) {
            logger.error("Exception when trying to delete item with ID: {}", itemId, e);
            return "error";
        }
    }


}
