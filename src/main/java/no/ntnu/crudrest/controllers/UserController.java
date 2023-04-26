package no.ntnu.crudrest.controllers;

import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.models.User;
import no.ntnu.crudrest.repositories.OrderRepository;
import no.ntnu.crudrest.service.AccessUserService;
import no.ntnu.crudrest.service.ProductService;
import no.ntnu.crudrest.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Controller
public class UserController {
    @Autowired
    private AccessUserService userService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;


    @GetMapping("users/{username}/orders")
    public String getUserOrders(Model model, @PathVariable String username) {
        model.addAttribute("cartProducts", shoppingCartService.getAmountInCart());
        model.addAttribute("user", userService.getSessionUser());
        User user = userService.getSessionUser();
        if (!user.getUsername().equals(username)) {
            return "no-access";
        }
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "UserOrders";
    }

}
