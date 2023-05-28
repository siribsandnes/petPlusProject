package no.ntnu.crudrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "ShoppingCartController", description = "Handles checkout-related operations")
public class ShoppingCartController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/shoppingCart")
    @Operation(
            summary = "View the shopping cart",
            description = "Retrieves the products in the shopping cart and calculates the total price."
    )
    public String shoppingCart(Model model) {
        model.addAttribute("products", shoppingCartService.getProductsInCart());
        model.addAttribute("total", shoppingCartService.getTotal().toString());
        model.addAttribute("shipping", shoppingCartService.checkShippingCost(shoppingCartService.getTotal()).toString());
        model.addAttribute("finalPrice", shoppingCartService.getTotal().add(shoppingCartService.checkShippingCost(shoppingCartService.getTotal())).toString());

        return "/shoppingCart";
    }

    @GetMapping("/shoppingCart/addProduct/{productId}")
    @Operation(
            summary = "Add a product to the shopping cart",
            description = "Adds a product with the specified ID to the shopping cart."
    )
    public String addProductToCart(@PathVariable("productId") int productId) {
            productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart/addProduct/{productId}")
    @Operation(
            summary = "Add a product to the shopping cart",
            description = "Adds a product with the specified ID to the shopping cart."
    )
    public String addProductToCart(@PathVariable("productId") int productId, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        //just so the link won't get extremely long
        String updatedReferer = referer.replaceAll("[?&]cartProducts=[^&]+", "");
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        return "redirect:" + updatedReferer;
    }

    @GetMapping("/shoppingCart/removeProduct/{productId}")
    @Operation(
            summary = "Remove a product from the shopping cart",
            description = "Removes a product with the specified ID from the shopping cart."
    )
    public String removeProductFromCart(@PathVariable("productId") int productId) {
        productService.findById(productId).ifPresent(product -> shoppingCartService.removeProduct(product));
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart/checkout")
    @Operation(
            summary = "Checkout and proceed to payment",
            description = "Processes the checkout request and redirects to the payment page."
    )
    public String checkout(HttpServletRequest request, HttpSession session)  {
        //session set attribute so it can be used in the processPayment() in ShoppingCartService
        session.setAttribute("streetAddress", request.getParameter("streetAddress"));
        session.setAttribute("postalCode", request.getParameter("postalCode"));
        session.setAttribute("city", request.getParameter("city"));
        session.setAttribute("firstName", request.getParameter("firstName"));
        session.setAttribute("lastName", request.getParameter("lastName"));
        session.setAttribute("phoneNumber", request.getParameter("phoneNumber"));


        return "redirect:/shoppingCart/payment";
    }

    @PostMapping("/shoppingCart/payment")
    @Operation(
            summary = "Process the payment",
            description = "Processes the payment request and handles success or failure."
    )
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
    @Operation(
            summary = "View the payment page",
            description = "Displays the payment page with the total price and shipping cost."
    )
    public String payment(Model model) {
        model.addAttribute("total", shoppingCartService.getTotal().toString());
        model.addAttribute("shipping", shoppingCartService.checkShippingCost(shoppingCartService.getTotal()).toString());
        model.addAttribute("finalPrice", shoppingCartService.getTotal().add(shoppingCartService.checkShippingCost(shoppingCartService.getTotal())).toString());

        // check if there are any products in the shopping cart, if so can go to payment else get sent back to shopping cart
        if (shoppingCartService.getProductsInCart().isEmpty()) {
            return "redirect:/shoppingCart";
        }
        return "payment";
    }

    @GetMapping("/shoppingCart/paymentSuccess")
    @Operation(
            summary = "View the payment success page",
            description = "Displays a page indicating that the payment was successful."
    )
    public String showPaymentSuccess() {
        return "paymentSuccess";
    }
}
