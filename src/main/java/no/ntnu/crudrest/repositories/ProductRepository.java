package no.ntnu.crudrest.repositories;


import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.models.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Handle SQL database access, for products.
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {
  // SELECT * FROM product WHERE name = ?
  Product findProductByProductName(String productName);

  Iterable<Product> findAllByProductNameContaining(String productName);

  Iterable<Product> findAllByProductPriceGreaterThanAndProductNameContaining(float productPrice, String productName);

  Page<Product> findAll(Pageable pageable);
}

