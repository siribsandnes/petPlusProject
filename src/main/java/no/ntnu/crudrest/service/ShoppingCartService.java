package no.ntnu.crudrest.service;

import jakarta.servlet.http.HttpServletRequest;
import no.ntnu.crudrest.exception.NotEnoughProductsInStockException;
import no.ntnu.crudrest.exception.PaymentFailedException;
import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.models.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product);

    void removeProduct(Product product);

    Map<Product, Integer> getProductsInCart();


    Order processPayment(HttpServletRequest request) throws NotEnoughProductsInStockException, PaymentFailedException;

    BigDecimal getTotal();

    int getAmountInCart();
}
