package no.ntnu.crudrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
public class Product {
  @Id
  @GeneratedValue
  private int productId;
  private String productName;
  private float productPrice;
  private int productAmount;

  @ManyToMany(mappedBy = "products")
  private Set<ProductCategory> productCategories = new HashSet<>();



  public Product() {
  }

  public Product(int productId, String productName, float productPrice, int productAmount) {
    this.productId = productId;
    this.productName = productName;
    this.productAmount = productAmount;
    this.productPrice = productPrice;
  }

  /**
   * Check if this object is a valid product
   *
   * @return True if the product is valid, false otherwise
   */
  @JsonIgnore
  public boolean isValid() {
    return productName != null && !productName.equals("");
  }

  public int getProductId() {
    return productId;
  }

  public Set<ProductCategory> getProductCategory(){return productCategories;}

  public void setProductCategory(Set<ProductCategory> productCategories){this.productCategories = productCategories;}

  public void addProductCategory(ProductCategory productCategory){productCategories.add(productCategory);}

  public void setProductId(int id) {
    this.productId = id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  /**
   * Returns the price of the product
   *
   * @return Price of product
   */
  public float getProductPrice() {
    return productPrice;
  }

  /**
   * Set the price of the product
   *
   * @param price Price of the product
   */
  public void setProductPrice(int price) {
    this.productPrice = price;
  }

  public int getProductAmount() {
    return productAmount;
  }

  public void setProductAmount(int amount) {
    this.productAmount = amount;
  }

}