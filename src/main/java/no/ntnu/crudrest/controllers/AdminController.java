package no.ntnu.crudrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.repositories.OrderRepository;
import no.ntnu.crudrest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
@Controller
@Tag(name = "AdminController", description = "Handles admin operations")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderRepository orderRepository;



    @GetMapping("admin")
    @Operation(
            summary = "Show admin page",
            description = "Displays the admin page"
    )
    public String adminPage(Model model) {
        model.addAttribute("products", productService.getAll());
        return "admin";
    }

    @GetMapping("admin/orders")
    @Operation(
            summary = "Show order page for all orders",
            description = "Displays the admin order page which shows all orders"
    )
    public String adminOrderPage(Model model) {
        model.addAttribute("products", productService.getAll());
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "UserOrders";
    }

    @PostMapping("admin/restock")
    @Operation(
            summary = "Restock products",
            description = "Shows restocks page and allows admin to restock the specified product with the given quantity"
    )
    public String adminRestockPage(@RequestParam Integer product_id, @RequestParam Integer quantity, RedirectAttributes rm, Model model) {
        Optional<Product> productOpt = productService.findById(product_id);
        if (quantity <= 0){
            //restock shouldn't remove stock if negative
            return "redirect:/admin";
        }
        if (productOpt.isEmpty()) {
                //Gives user an error if they type a non-existent product_ID
                rm.addFlashAttribute("errorMessage", "ProductID does not exist!");
                return "redirect:/admin";
        }
        Product product = productOpt.get();
        product.setProductAmountInStock(product.getProductAmountInStock() + quantity);
        productService.save(product);
        return "redirect:/admin";
    }


}
