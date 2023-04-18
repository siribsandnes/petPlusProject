package no.ntnu.crudrest.service;

import no.ntnu.crudrest.exception.NotEnoughProductsInStockException;
import no.ntnu.crudrest.models.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product);

    void removeProduct(Product product);

    Map<Product, Integer> getProductsInCart();

    void checkout() throws NotEnoughProductsInStockException;

    BigDecimal getTotal();
}
