package no.ntnu.crudrest.controllers;


import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Controller for all HTML pages.
 */
@Controller
public class PageController {
  Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

  @Autowired
  private ProductService productService;

//  /**
//   * Get all products stored in the database
//   *
//   * @return
//   */
//  @GetMapping("/api/products")
//  public String getHome(Model model) {
//    model.addAttribute("products", productService.getFirst(5));
//    return "products";
//  }
}
