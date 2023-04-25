package no.ntnu.crudrest.controllers;

import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.repositories.OrderRepository;
import no.ntnu.crudrest.service.AccessUserService;
import no.ntnu.crudrest.service.ProductService;
import no.ntnu.crudrest.service.ShoppingCartService;
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
public class AdminController {
    @Autowired
    private AccessUserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("admin")
    public String adminPage(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        model.addAttribute("products", productService.getAll());
        return "admin";
    }

    @GetMapping("admin/orders")
    public String adminOrderPage(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        model.addAttribute("products", productService.getAll());
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "UserOrders";
    }

    @PostMapping("admin/restock")
    public String adminRestockPage(@RequestParam Integer product_id, @RequestParam Integer quantity, RedirectAttributes rm) {
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

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setProductAmountInStock(product.getProductAmountInStock() + quantity);
            productService.save(product);
        }
        return "redirect:/admin";
    }


}
