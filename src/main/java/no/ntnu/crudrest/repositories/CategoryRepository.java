package no.ntnu.crudrest.repositories;

import no.ntnu.crudrest.models.ProductCategory;
import no.ntnu.crudrest.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<ProductCategory, Integer> {
    ProductCategory findByName(String name);
}
