package no.ntnu.crudrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST API controller for store
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductRepository productRepository;

  private static final Logger logger = LoggerFactory.getLogger(ProductController.class.getSimpleName());

  public ProductController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @GetMapping
  @Operation(
      summary = "Get all products",
      description = "List of all products currently stored in the collection"
  )
  public ResponseEntity<Object> getAll() {
    logger.error("Getting all products");
    Iterable<Product> products = productRepository.findAll();
    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @Operation(
          summary = "Get product with ID",
          description = "Gets one product with the corresponding ID"
  )
  Product one(@PathVariable int id) throws NoSuchElementException {
    return productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException());
  }

  @DeleteMapping("/{id}")
  @Operation(
          summary = "Delete product with ID",
          description = "Deletes the product with the corresponding ID"
  )
  void deleteProduct(@PathVariable int id) {
    productRepository.deleteById(id);
  }
}