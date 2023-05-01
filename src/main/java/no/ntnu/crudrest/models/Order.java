package no.ntnu.crudrest.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

    @ElementCollection
    private Map<Integer, Integer> quantities = new HashMap<>();

    @Column(nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    @ManyToOne
    private Address address;


    //GETTERS AND SETTERS
    public void addProduct(Product product, int quantity) {
        products.add(product);
        quantities.put(product.getProductId(), quantity);
    }
    public int getQuantity(Product product) {
        return quantities.getOrDefault(product.getProductId(), 0);
    }


    private Double totalCost;

    public Integer getId() {
        return orderId;
    }

    public void setId(Integer orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products.addAll(products);
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }


}
