package no.ntnu.crudrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.crudrest.dto.SignupDto;
import no.ntnu.crudrest.dto.UserProfileDto;
import no.ntnu.crudrest.models.User;
import no.ntnu.crudrest.service.AccessUserService;
import no.ntnu.crudrest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
@Tag(name = "AuthenticationController", description = "Takes care of user registration and authentication")
public class AuthenticationController {

    @Autowired
    private AccessUserService userService;

    @Autowired
    private ProductService productService;

    /**
     * Home page.
     *
     * @return the index.html Thymeleaf template name
     */
    @GetMapping("/")
    @Operation(
            summary = "Show homepage",
            description = "Shows the homepage and adds 5 products to the top category list."
    )
    public String home(Model model) {
        model.addAttribute("products", productService.getFirst(5));
        return "index";
    }

    @GetMapping("/login")
    @Operation(
            summary = "Show login form",
            description = "Displays the login form"
    )
    public String loginForm() {
        return "login";
    }

    @GetMapping("/forgotPassword")
    @Operation(
            summary = "Show password reset form",
            description = "Displays the password reset form"
    )
    public String resetPassword(Model model){return "reset-password";}


    /**
     * Sign-up form
     *
     * @return NAme of Thymeleaf template to use
     */
    @GetMapping("/signup")
    @Operation(
            summary = "Show sign-up form",
            description = "Displays the sign-up form"
    )
    public String signupForm() {
        return "signup-form";
    }

    /**
     * This method processes data received from the sign-up form (HTTP POST)
     *
     * @return NAme of the template for the result page
     */
    @PostMapping("/signup")
    @Operation(
            summary = "Process sign-up form",
            description = "Processes the data received from the sign-up form (HTTP POST)"
    )
    public String signupProcess(@ModelAttribute SignupDto signupData, Model model) {
        model.addAttribute("signupData", signupData);
        if (!signupData.getPassword().equals(signupData.getRepeat())) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "signup-form";
        }
        String errorMessage = userService.tryCreateNewUser(signupData.getUsername(), signupData.getPassword());
        if (errorMessage == null) {
            return "signup-success";
        } else {
            model.addAttribute("errorMessage", errorMessage);
            return "signup-form";
        }
    }



    /**
     * Show profile page for a user
     *
     * @param model    Model for passing data to Thymeleaf
     * @param username Username of the user
     * @return
     */

    @GetMapping("users/{username}")
    @Operation(
            summary = "Show user profile",
            description = "Displays the profile page for a user"
    )
    public String userPage(Model model, @PathVariable String username) {
        return handleProfilePageRequest(username, null, model);
    }

    /**
     * This method handles HTTP POST - user submits changes to his/her profile
     *
     * @param model    Model for passing data to Thymeleaf
     * @param username Username of the user
     * @return name of the Thymeleaf template to render the result
     */
    @PostMapping("users/{username}")
    @Operation(
            summary = "Update user profile",
            description = "Handles HTTP POST - user submits changes to his/her profile"
    )
    public String userPagePost(@ModelAttribute UserProfileDto profileData, @PathVariable String username, Model model) {
        return handleProfilePageRequest(username, profileData, model);
    }

    /**
     * Handler GET or POST request to the /users/{username} page. When the POST data is present,
     * update user profile data. Also checks if we are accessing a page which is allowed for
     * this user.
     *
     * @param username Username of the user profile to load
     * @param postData HTTP POST data submitted in the request. When not null, user profile
     *                 will be updated.
     * @param model    The model to put successMessage or errorMessage in
     * @return Name of the template to render: user on success, no-access if the request
     * is unauthorized.
     */

    private String handleProfilePageRequest(String username, UserProfileDto postData, Model model) {
        User authenticatedUser = userService.getSessionUser();
        if (authenticatedUser != null && authenticatedUser.getUsername().equals(username)) {
            model.addAttribute("user", authenticatedUser);
            if (postData != null) {
                if (userService.updateProfile(authenticatedUser, postData)) {
                    model.addAttribute("successMessage", "Profile updated!");
                } else {
                    model.addAttribute("errorMessage", "Could not update profile data!");
                }
            }
            return "user";
        } else {
            return "no-access";
        }
    }
}
