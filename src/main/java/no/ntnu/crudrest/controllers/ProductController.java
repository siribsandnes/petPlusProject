package no.ntnu.crudrest.controllers;

import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
            return "notFoundError";
        }
    }

    @GetMapping("/products/dogs")
    public String getDogPage(Model model) {
        Iterable<Product> products = productService.getAllByProductCategoryContaining("dog");
        model.addAttribute("products", products);
        return "products";
    }
    @GetMapping("/products/cats")
    public String getCatPage(Model model) {
        Iterable<Product> products = productService.getAllByProductCategoryContaining("cat");
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/smallpets")
    public String getSmallPetsPage(Model model) {
        Iterable<Product> products = productService.getAllByProductCategoryContaining("small pets");
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/sale")
    public String getSalePage(Model model) {
        Iterable<Product> products = productService.getAllByProductCategoryContaining("sale");
        model.addAttribute("products", products);
        return "products";
    }


    /**
     * Method for handling searches.
     *
     * Searches both Product Name and ProductCategory for products containing the query.
     *
     * If no search query is provided returns all products.
     *
     * @param model model object to be populated with data for the view
     * @param searchQuery query we are searching for
     * @return the name of the view to be rendered
     */
    @GetMapping("/products/search")
    public String getProductPage(Model model, @RequestParam(required = false) String searchQuery) {
        Iterable<Product> products;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            products = productService.searchProducts(searchQuery);
        } else {
            products = productService.getAll();
        }
        model.addAttribute("products", products);
        return "products";
    }

}
