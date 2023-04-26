package no.ntnu.crudrest.controllers;

import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.service.AccessUserService;
import no.ntnu.crudrest.service.ProductService;
import no.ntnu.crudrest.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
@Controller
public class ProductController {
    @Autowired
    private AccessUserService userService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/products")
    public String getProductPage(Model model) {
        model.addAttribute("cartProducts", shoppingCartService.getAmountInCart());
        model.addAttribute("user", userService.getSessionUser());
        model.addAttribute("products", productService.getAll());
        return "products";
    }

    @GetMapping(path = "/products/{id}")
    public String getProductById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("cartProducts", shoppingCartService.getAmountInCart());
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

}
