package no.ntnu.crudrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Entity
public class ProductCategory {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @ManyToMany
    @JoinTable(name = "productcategory_product",
            joinColumns = @JoinColumn(name = "productCategory_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    public ProductCategory() {
    }

    public ProductCategory(String name){
        this.name = name;
    }
    @JsonIgnore // This annotation makes sure we don't include "valid" in the JSON
    public boolean isValid() {
        return !"".equals(name) && (id == null || id > 0);
    }

    //GETTERS AND SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String genre) {
        this.name = genre;
    }

    public Set<Product> getproducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

}
