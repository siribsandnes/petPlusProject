package no.ntnu.crudrest.repositories;


import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.models.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Handle SQL database access, for products.
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {
  // SELECT * FROM product WHERE name = ?
  Product findProductByProductName(String productName);

  Iterable<Product> findAllByProductNameContaining(String productName);

  Iterable<Product> findAllByProductPriceGreaterThanAndProductNameContaining(float productPrice, String productName);

  Iterable<Product> findAllByProductCategoriesNameContaining(String categoryName);
  Page<Product> findAll(Pageable pageable);
}

