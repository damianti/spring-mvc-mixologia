/**
 AdminController.java:
 The AdminController class is a controller class that handles requests related to the administration zone.
 */
package hac.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
public class AdminController extends BaseController {

    /**
     * Administration zone index.
     * Note that we can access current logged user just by adding the Principal
     * parameter
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping
    public String adminIndex(Model model) {
        addCommonAttributes(model);
        model.addAttribute("currentPage", "admin");
        return "admin/index";
    }

    /**
     * Handles the GET request for the logout functionality.
     *
     * @return a redirect to the home page
     */
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }



}