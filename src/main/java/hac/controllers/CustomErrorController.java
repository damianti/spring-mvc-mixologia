/**
 CustomErrorController.java:
 The CustomErrorController class is a controller class that handles custom error pages for different HTTP status codes.
 */
package hac.controllers;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController extends BaseController implements ErrorController {
    /**
     * Handles the error page requests and displays the appropriate error message based on the HTTP status code.
     *
     * @param request the HttpServletRequest object containing the request information
     * @param model   the Model object to add attributes to
     * @return the view name for the error page
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            String errorMessage = ErrorMessages.ERROR_MESSAGES.getOrDefault(statusCode, ErrorMessages.ERROR_MESSAGES.get(-1));
            statusCode = statusCode ==403 ? 404 : statusCode;
            model.addAttribute("errorCode", statusCode);
            model.addAttribute("errorMessage", errorMessage);
        } else {
            model.addAttribute("errorCode", "Unknown");
            model.addAttribute("errorMessage", ErrorMessages.ERROR_MESSAGES.get(-2));
        }
        addCommonAttributes(model);
        return "error";
    }

    /**
     * The ErrorMessages class contains predefined error messages for different HTTP status codes and error scenarios.
     */
    public static class ErrorMessages {
        public static final Map<Integer, String> ERROR_MESSAGES = new HashMap<>();

        static {
            ERROR_MESSAGES.put(HttpStatus.NOT_FOUND.value(), "We're sorry, but the page you were trying to view does private or not exist.");
            ERROR_MESSAGES.put(HttpStatus.INTERNAL_SERVER_ERROR.value(), "We're currently experiencing technical difficulties. Please try again later.");
            ERROR_MESSAGES.put(HttpStatus.UNAUTHORIZED.value(), "You are not authorized to view this page. Please log in and try again.");
            ERROR_MESSAGES.put(HttpStatus.FORBIDDEN.value(), "We're sorry, but the page you were trying to view does private or not exist.");
            ERROR_MESSAGES.put(HttpStatus.BAD_REQUEST.value(), "We received a bad request. Please check your input and try again.");
            ERROR_MESSAGES.put(-1, "An unexpected error occurred. Please try again later.");
            ERROR_MESSAGES.put(-2, "An unknown error occurred. Please try again later.");
        }
    }
}
