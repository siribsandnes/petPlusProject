package no.ntnu.crudrest.service;

import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

  public Iterable<Product> getAllByProductCategoryContaining(String category) {
    return productRepository.findAllByProductCategoriesNameContaining(category);
  }

  public Iterable<Product> searchProducts(String query) {
    return productRepository.findAllByProductNameContainingOrProductCategoriesNameContaining(query, query);
  }

  /**
   * Gets the n first products stored in the database
   * @param n - the amount of products to return
   * @return n first products in the database
   */
  public Iterable<Product> getFirst(int n) {
    return productRepository.findAll(PageRequest.of(0, n));
  }

  public Optional<Product> findById(Integer id){
    return productRepository.findById(id);
  }

  public void save(Product product) {
    productRepository.save(product);
  }

}