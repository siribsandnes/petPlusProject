package no.ntnu.crudrest.controllers;

import no.ntnu.crudrest.dto.SignupDto;
import no.ntnu.crudrest.dto.UserProfileDto;
import no.ntnu.crudrest.exception.NotEnoughProductsInStockException;
import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.models.User;
import no.ntnu.crudrest.repositories.OrderRepository;
import no.ntnu.crudrest.service.ShoppingCartService;
import no.ntnu.crudrest.service.AccessUserService;
import no.ntnu.crudrest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * A controller serving our HTML pages
 */
@Controller
public class HtmlPageController {
    @Autowired
    private AccessUserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderRepository orderRepository;



    /**
     * Home page.
     *
     * @return the index.html Thymeleaf template name
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        return "index";
    }

    /**
     * Show profile page for a user
     *
     * @param model    Model for passing data to Thymeleaf
     * @param username Username of the user
     * @return
     */
    @GetMapping("users/{username}")
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


    @GetMapping("admin")
    public String adminPage(Model model) {
        // We still need the user for the navigation, even when we don't use it for the main content
        model.addAttribute("user", userService.getSessionUser());
        return "admin";
    }

        @GetMapping("/products")
        public String getProductPage(Model model) {
            model.addAttribute("user", userService.getSessionUser());
            model.addAttribute("products", productService.getFirst(5));
            return "products";
        }

    @GetMapping(path = "/products/{id}")
    public String getproductById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.getSessionUser());
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            return "product";
        } else {
            return "error"; // or some other error page
        }
    }



    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }


    /**
     * Sign-up form
     *
     * @return NAme of Thymeleaf template to use
     */
    @GetMapping("/signup")
    public String signupForm() {
        return "signup-form";
    }

    /**
     * This method processes data received from the sign-up form (HTTP POST)
     *
     * @return NAme of the template for the result page
     */
    @PostMapping("/signup")
    public String signupProcess(@ModelAttribute SignupDto signupData, Model model) {
        model.addAttribute("signupData", signupData);
        String errorMessage = userService.tryCreateNewUser(signupData.getUsername(), signupData.getPassword());
        if (errorMessage == null) {
            return "signup-success";
        } else {
            model.addAttribute("errorMessage", errorMessage);
            return "signup-form";
        }
    }

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        model.addAttribute("products", shoppingCartService.getProductsInCart());
        model.addAttribute("total", shoppingCartService.getTotal().toString());
        return "/shoppingCart";
    }

    @GetMapping("/shoppingCart/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("user", userService.getSessionUser());
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        return "redirect:/shoppingCart";
    }

    @GetMapping("/shoppingCart/removeProduct/{productId}")
    public String removeProductFromCart(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("user", userService.getSessionUser());
        productService.findById(productId).ifPresent(product -> shoppingCartService.removeProduct(product));
        return "redirect:/shoppingCart";
    }

    @GetMapping("/shoppingCart/checkout")
    public String checkout(Model model) {
        try {
            shoppingCartService.checkout();
        } catch (NotEnoughProductsInStockException e) {
            model.addAttribute("outOfStockMessage", e.getMessage());
            return "forward:/shoppingCart";
        }
        return "redirect:/shoppingCart";
    }


    @GetMapping("users/{username}/orders")
    public String getUserOrders(Model model, @PathVariable String username) {
        model.addAttribute("user", userService.getSessionUser());
        User user = userService.getSessionUser();
        if (!user.getUsername().equals(username)) {
            return "no-access";
        }
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "userOrders";
    }



}
