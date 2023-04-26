package no.ntnu.crudrest.controllers;

import jakarta.servlet.http.HttpServletRequest;
import no.ntnu.crudrest.exception.NotEnoughProductsInStockException;
import no.ntnu.crudrest.service.AccessUserService;
import no.ntnu.crudrest.service.ProductService;
import no.ntnu.crudrest.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ShoppingCartController {

    @Autowired
    private AccessUserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        model.addAttribute("cartProducts", shoppingCartService.getAmountInCart());
        model.addAttribute("user", userService.getSessionUser());
        model.addAttribute("products", shoppingCartService.getProductsInCart());
        model.addAttribute("total", shoppingCartService.getTotal().toString());
        return "/shoppingCart";
    }

    @GetMapping("/shoppingCart/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("cartProducts", shoppingCartService.getAmountInCart());
        model.addAttribute("user", userService.getSessionUser());
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") int productId, HttpServletRequest request) {
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/shoppingCart/removeProduct/{productId}")
    public String removeProductFromCart(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("cartProducts", shoppingCartService.getAmountInCart());
        model.addAttribute("user", userService.getSessionUser());
        productService.findById(productId).ifPresent(product -> shoppingCartService.removeProduct(product));
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart/checkout")
    public String checkout(Model model,HttpServletRequest request) {
        model.addAttribute("cartProducts", shoppingCartService.getAmountInCart());
        model.addAttribute("user", userService.getSessionUser());
        try {
            shoppingCartService.checkout(request);
            return "checkoutSuccess";
        } catch (NotEnoughProductsInStockException e) {
            model.addAttribute("outOfStockMessage", e.getMessage());
            return "forward:/shoppingCart";
        }
    }
}
