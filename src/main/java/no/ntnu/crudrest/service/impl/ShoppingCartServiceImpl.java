package no.ntnu.crudrest.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import no.ntnu.crudrest.exception.NotEnoughProductsInStockException;


import no.ntnu.crudrest.exception.PaymentFailedException;
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
     * @param product product being added
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
     * @param product product being removed
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
     *
     * @throws NotEnoughProductsInStockException if product is out of stock
     * @throws PaymentFailedException if payment info isn't valid
     */
    @Override
    public Order processPayment(HttpServletRequest request) throws NotEnoughProductsInStockException, PaymentFailedException {


        // check if payment info is valid, if not throw an error.
        //This is just a very simple check and most things will pass because this is a fake web shop, but it will throw an error if any of them are missing or equal 0 or null
        String cardNumber = request.getParameter("cardNumber");
        String cvv = request.getParameter("cvv");
        String expiryDate = request.getParameter("expiryDate");

        if (cardNumber == null || cardNumber.isEmpty() || cardNumber.equals("0")|| cvv == null || cvv.isEmpty() || cvv.equals("0") || expiryDate == null || expiryDate.isEmpty()|| expiryDate.equals("0")) {
            throw new PaymentFailedException("Invalid payment information");
        }

        Order order = new Order();
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            // check if the product is still in stock
            Optional<Product> product = productRepository.findById(entry.getKey().getProductId());
            if (product.isPresent() && product.get().getProductAmountInStock() < entry.getValue()) {
                throw new NotEnoughProductsInStockException(product);
            }
            // update the product stock and add it to the order
            entry.getKey().setProductAmountInStock(product.get().getProductAmountInStock() - entry.getValue());
            order.addProduct(entry.getKey(), entry.getValue());
        }
        productRepository.saveAll(products.keySet());
        order.setUser(userService.getSessionUser());
        Address address = new Address();
        address.setStreetAddress((String) request.getSession().getAttribute("streetAddress"));
        address.setPostalCode((String) request.getSession().getAttribute("postalCode"));
        address.setCity((String) request.getSession().getAttribute("city"));
        addressRepository.save(address);
        order.setAddress(address);
        order.setProducts(products.keySet());
        Double totalCost = products.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getProductPrice() * entry.getValue())
                .sum();
        order.setTotalCost(totalCost);
        orderRepository.save(order);
        products.clear();
        return order;
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

    @Override
    public int getAmountInCart(){
        return products.values().stream().mapToInt(i -> i).sum();

    }
}