package no.ntnu.crudrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Schema(description = "The product entity")
public class Product {

  @Id
  @GeneratedValue
  @Schema(description = "The unique identifier of the product")
  private int productId;
  @Schema(description = "The name of the product")
  private String productName;
  @Schema(description = "The price of the product")
  private float productPrice;

  @Column(columnDefinition = "TEXT")
  @Schema(description = "The description of the product")
  private String productDescription;
  @Schema(description = "The amount of the product in stock")
  private int productAmountInStock;
  @Schema(description = "The filepath to the picture of the product")
  private String productPicture;
  @Schema(description = "The alternative description of the product picture")
  private String productPictureAlt;


  @ManyToMany(mappedBy = "products")
  private Set<ProductCategory> productCategories = new HashSet<>();



  public Product() {
  }

  public Product(int productId, String productName, String productDescription, float productPrice, int productAmountInStock, String productPicture, String productPictureAlt) {
    this.productId = productId;
    this.productName = productName;
    this.productAmountInStock = productAmountInStock;
    this.productPrice = productPrice;
    this.productDescription = productDescription;
    this.productPicture = productPicture;
    this.productPictureAlt = productPictureAlt;
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

  //GETTERS AND SETTERS

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

  public int getProductAmountInStock() {
    return productAmountInStock;
  }

  public void setProductAmountInStock(int amount) {
    this.productAmountInStock = amount;
  }

  public String getProductDescription(){return productDescription;}

  public void setProductDescription(String description){this.productDescription = description;}

  public String getProductPicture(){return productPicture;}

  public void setProductPicture(String pictureFilePath){this.productPicture = pictureFilePath;}

  public String getProductPictureAlt(){return productPictureAlt;}

  public void setProductPictureAlt(String pictureDescription){this.productPictureAlt = pictureDescription;}



  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Product)) return false;
    Product other = (Product) obj;
    return productId == other.productId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId);
  }

}