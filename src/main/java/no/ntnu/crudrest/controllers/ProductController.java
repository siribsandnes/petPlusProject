package no.ntnu.crudrest.controllers;

import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("/products")
    public String getProductPage(Model model) {
        model.addAttribute("products", productService.getAll());
        return "products";
    }

    @GetMapping(path = "/products/{id}")
    public String getProductById(@PathVariable("id") Integer id, Model model) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            return "product";
        } else {
            return "error";
        }
    }

}
