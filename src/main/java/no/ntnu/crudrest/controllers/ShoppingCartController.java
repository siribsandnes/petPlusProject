package no.ntnu.crudrest.controllers;

import jakarta.servlet.http.HttpServletRequest;
import no.ntnu.crudrest.exception.NotEnoughProductsInStockException;
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
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        model.addAttribute("products", shoppingCartService.getProductsInCart());
        model.addAttribute("total", shoppingCartService.getTotal().toString());
        return "/shoppingCart";
    }

    @GetMapping("/shoppingCart/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") int productId, Model model) {
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") int productId, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String updatedReferer = referer.replaceAll("[?&]cartProducts=[^&]+", "");
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        System.out.println(updatedReferer);
        return "redirect:" + updatedReferer;
    }

    @GetMapping("/shoppingCart/removeProduct/{productId}")
    public String removeProductFromCart(@PathVariable("productId") int productId, Model model) {
        productService.findById(productId).ifPresent(product -> shoppingCartService.removeProduct(product));
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart/checkout")
    public String checkout(Model model,HttpServletRequest request) {
        try {
            shoppingCartService.checkout(request);
            return "checkoutSuccess";
        } catch (NotEnoughProductsInStockException e) {
            model.addAttribute("outOfStockMessage", e.getMessage());
            return "forward:/shoppingCart";
        }
    }
}
