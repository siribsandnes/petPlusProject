package no.ntnu.crudrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "ProductController", description = "Handles product-related operations")
public class ProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("/products")
    @Operation(
            summary = "Show all products",
            description = "Displays all available products"
    )
    public String getProductPage(Model model) {
        model.addAttribute("products", productService.getAll());
        return "products";
    }

    @GetMapping(path = "/products/{id}")
    @Operation(
            summary = "Get product by ID",
            description = "Gets the product with the specified ID"
    )
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
    @Operation(
            summary = "Show dog products",
            description = "Displays products related to dogs"
    )
    public String getDogPage(Model model) {
        Iterable<Product> products = productService.getAllByProductCategoryContaining("dog");
        model.addAttribute("products", products);
        return "products";
    }
    @GetMapping("/products/cats")
    @Operation(
            summary = "Show cat products",
            description = "Displays products related to cats"
    )
    public String getCatPage(Model model) {
        Iterable<Product> products = productService.getAllByProductCategoryContaining("cat");
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/smallpets")
    @Operation(
            summary = "Show small pets products",
            description = "Displays products related to small pets"
    )
    public String getSmallPetsPage(Model model) {
        Iterable<Product> products = productService.getAllByProductCategoryContaining("small pets");
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/sale")
    @Deprecated
    @Operation(
            summary = "Show products on sale",
            description = "This endpoint is deprecated and no longer in use"
    )
    public String getSalePage(Model model) {
        Iterable<Product> products = productService.getAllByProductCategoryContaining("sale");
        model.addAttribute("products", products);
        return "products";
    }


    /**
     * Method for handling searches.
     * Searches both Product Name and ProductCategory for products containing the query.
     * If no search query is provided returns all products.
     *
     * @param model model object to be populated with data for the view
     * @param searchQuery query we are searching for
     * @return the name of the view to be rendered
     */
    @GetMapping("/products/search")
    @Operation(
            summary = "Search products",
            description = "Searches for products based on the given query"
    )
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
