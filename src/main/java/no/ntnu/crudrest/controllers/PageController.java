package no.ntnu.crudrest.controllers;


import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller for all HTML pages.
 */
@Controller
public class PageController {
  @Autowired
  ProductService productService;


  /**
   * The `Home` page.
   *
   * @param model The model where the data will be stored
   * @return Name of the ThymeLeaf template to render
   */
  @GetMapping("/")
  public String getHome(Model model) {
    model.addAttribute("products", productService.getFirst(5));
    return "index";
  }
  @GetMapping(path = "/{id}")
  public String getproductById(@PathVariable("id") Integer id, Model model) {
    Product product = productService.findById(id);
    model.addAttribute("product", product);
    return "product";
  }
}