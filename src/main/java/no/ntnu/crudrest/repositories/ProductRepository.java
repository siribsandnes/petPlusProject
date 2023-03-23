package no.ntnu.crudrest.repositories;


import no.ntnu.crudrest.models.Product;
import org.springframework.data.repository.CrudRepository;

/**
 * Handle SQL database access, for products.
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {
  // SELECT * FROM product WHERE name = ?
  Iterable<Product> findAllByProductName(String productName);

  Iterable<Product> findAllByProductNameContaining(String productName);

  Iterable<Product> findAllByProductPriceGreaterThanAndProductNameContaining(float productPrice, String productName);
}
