package no.ntnu.crudrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.models.User;
import no.ntnu.crudrest.repositories.OrderRepository;
import no.ntnu.crudrest.service.AccessUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Controller
@Tag(name = "UserController", description = "Handles user-related operations")
public class UserController {
    @Autowired
    private AccessUserService userService;
    @Autowired
    private OrderRepository orderRepository;



    @GetMapping("users/{username}/orders")
    @Operation(
            summary = "Get user orders",
            description = "Retrieves the orders associated with the specified user."
    )
    public String getUserOrders(Model model, @PathVariable String username) {
        User user = userService.getSessionUser();
        if (!user.getUsername().equals(username)) {
            return "no-access";
        }
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "UserOrders";
    }

}
