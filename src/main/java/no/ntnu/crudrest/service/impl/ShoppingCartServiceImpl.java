package no.ntnu.crudrest.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import no.ntnu.crudrest.exception.NotEnoughProductsInStockException;


import no.ntnu.crudrest.models.Address;
import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.models.Product;
import no.ntnu.crudrest.repositories.AddressRepository;
import no.ntnu.crudrest.repositories.OrderRepository;
import no.ntnu.crudrest.repositories.ProductRepository;
import no.ntnu.crudrest.service.AccessUserService;
import no.ntnu.crudrest.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final AddressRepository addressRepository;
    private final AccessUserService userService;



    private Map<Product, Integer> products = new HashMap<>();

    @Autowired
    public ShoppingCartServiceImpl(ProductRepository productRepository, OrderRepository orderRepository, AddressRepository addressRepository, AccessUserService userService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.userService = userService;
    }

    /**
     * If product is in the map just increment quantity by 1.
     * If product is not in the map with, add it with quantity 1
     *
     * @param product
     */
    @Override
    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
    }

    /**
     * If product is in the map with quantity > 1, just decrement quantity by 1.
     * If product is in the map with quantity 1, remove it from map
     *
     * @param product
     */
    @Override
    public void removeProduct(Product product) {
        if (products.containsKey(product)) {
            if (products.get(product) > 1)
                products.replace(product, products.get(product) - 1);
            else if (products.get(product) == 1) {
                products.remove(product);
            }
        }
    }

    /**
     * @return unmodifiable copy of the map
     */
    @Override
    public Map<Product, Integer> getProductsInCart() {
        return Collections.unmodifiableMap(products);
    }

    /**
     * Checkout checks if there are enough products in stock to
     *
     * @throws NotEnoughProductsInStockException
     */
    @Override
    public void checkout(HttpServletRequest request) throws NotEnoughProductsInStockException {
        Optional<Product> product;

        Order order = new Order();
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            product = productRepository.findById(entry.getKey().getProductId());
            if (product.isPresent() && product.get().getProductAmountInStock() < entry.getValue()) {
                throw new NotEnoughProductsInStockException(product);
            }
            entry.getKey().setProductAmountInStock(product.get().getProductAmountInStock() - entry.getValue());
            order.addProduct(entry.getKey(), entry.getValue());
        }
        productRepository.saveAll(products.keySet());
        order.setUser(userService.getSessionUser());
        //We need to make a new address for the order even if the user has one
        // because they might change their stored address in the future but their old orders still need to have the old address
        Address address = new Address();
        address.setStreetAddress(request.getParameter("streetAddress"));
        address.setPostalCode(request.getParameter("postalCode"));
        address.setCity(request.getParameter("city"));
        addressRepository.save(address);
        order.setAddress(address);

        order.setProducts(products.keySet());
        Double totalCost = products.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getProductPrice() * entry.getValue())
                .sum();
        order.setTotalCost(totalCost);
        orderRepository.save(order);

        products.clear();
    }



    @Override
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            BigDecimal price = BigDecimal.valueOf(entry.getKey().getProductPrice());
            int quantity = entry.getValue();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
            total = total.add(subtotal);
        }
        return total;
    }
}