package no.ntnu.crudrest;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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

}