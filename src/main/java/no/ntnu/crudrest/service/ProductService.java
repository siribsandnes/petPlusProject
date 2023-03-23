package no.ntnu.crudrest.service;

import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Business logic related to products
 */
@Service
public class ProductService {
  @Autowired
  private ProductRepository productRepository;

  /**
   * Get all products currently stored in the database.
   *
   * @return All the products
   */
  public Iterable<Product> getAll() {
    return productRepository.findAll();
  }


  /**
   * Gets the n first products stored in the database
   * @param n - the amount of books to return
   * @return n first products in the database
   */
  public Iterable<Product> getFirst(int n) {
    return productRepository.findAll(PageRequest.of(0, n));
  }
}