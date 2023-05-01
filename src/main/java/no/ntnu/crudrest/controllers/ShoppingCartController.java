package no.ntnu.crudrest.controllers;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpSession;
import no.ntnu.crudrest.exception.NotEnoughProductsInStockException;
import no.ntnu.crudrest.exception.PaymentFailedException;
import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.service.ProductService;
import no.ntnu.crudrest.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("shipping", shoppingCartService.checkShippingCost(shoppingCartService.getTotal()).toString());
        model.addAttribute("finalPrice", shoppingCartService.getTotal().add(shoppingCartService.checkShippingCost(shoppingCartService.getTotal())).toString());

        return "/shoppingCart";
    }

    @GetMapping("/shoppingCart/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") int productId) {
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") int productId, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        //just so the link won't get extremely long
        String updatedReferer = referer.replaceAll("[?&]cartProducts=[^&]+", "");
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        return "redirect:" + updatedReferer;
    }

    @GetMapping("/shoppingCart/removeProduct/{productId}")
    public String removeProductFromCart(@PathVariable("productId") int productId) {
        productService.findById(productId).ifPresent(product -> shoppingCartService.removeProduct(product));
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart/checkout")
    public String checkout(HttpServletRequest request, HttpSession session)  {
        //session set attribute so it can be used in the processPayment() in ShoppingCartService
        //TODO: ADD PHONE NUMBER , FIRST NAME AND LAST NAME TO ORDERS
        session.setAttribute("streetAddress", request.getParameter("streetAddress"));
        session.setAttribute("postalCode", request.getParameter("postalCode"));
        session.setAttribute("city", request.getParameter("city"));
        session.setAttribute("firstName", request.getParameter("firstName"));
        session.setAttribute("lastName", request.getParameter("lastName"));
        session.setAttribute("phoneNumber", request.getParameter("phoneNumber"));


        return "redirect:/shoppingCart/payment";
    }

    @PostMapping("/shoppingCart/payment")
    public String processPayment(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Order order = shoppingCartService.processPayment(request);
            redirectAttributes.addFlashAttribute("orderId", order.getId());
            redirectAttributes.addFlashAttribute("totalCost", order.getTotalCost());

            return "redirect:/shoppingCart/paymentSuccess";
        } catch (NotEnoughProductsInStockException e) {
            //Error if out of stock
            model.addAttribute("outOfStockMessage", e.getMessage());
            return "paymentError";
        } catch (PaymentFailedException e) {
            //Error if bad payment information
            model.addAttribute("paymentFailedMessage", e.getMessage());
            return "paymentError";
        }
    }

    @GetMapping("/shoppingCart/payment")
    public String payment() {
        // check if there are any products in the shopping cart, if so can go to payment else get sent back to shopping cart
        if (shoppingCartService.getProductsInCart().isEmpty()) {
            return "redirect:/shoppingCart";
        }
        return "payment";
    }

    @GetMapping("/shoppingCart/paymentSuccess")
    public String showPaymentSuccess() {
        return "paymentSuccess";
    }
}
