package no.ntnu.crudrest.service;

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
public class ShoppingCartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccessUserService userService;

    private Map<Product, Integer> products = new HashMap<>();


    /**
     * If product is in the map just increment quantity by 1.
     * If product is not in the map with, add it with quantity 1
     *
     * @param product product being added
     */

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

    public Map<Product, Integer> getProductsInCart() {
        return Collections.unmodifiableMap(products);
    }

    /**
     * Checks the provided payment information and throws exception in case of invalid information.
     *
     * Since this is a simple project that doesn't actually take any sort of payment the check is very simple.
     * It will throw an error if any of the provided information is null, empty or just 0.
     *
     * @param request the request from the order being made
     * @throws PaymentFailedException for invalid payment information
     */

    public void checkValidPayment(HttpServletRequest request) throws PaymentFailedException{

        String cardNumber = request.getParameter("cardNumber");
        String cvv = request.getParameter("cvv");
        String expiryDate = request.getParameter("expiryDate");

        if (cardNumber == null || cardNumber.isEmpty() || cardNumber.equals("0")) {
            throw new PaymentFailedException("Invalid payment information at Card Number");
        }
        else if (cvv == null || cvv.isEmpty() || cvv.equals("0")){
            throw new PaymentFailedException("Invalid payment information at CVV");
        }
        else if(expiryDate == null || expiryDate.isEmpty()|| expiryDate.equals("0")){
            throw new PaymentFailedException("Invalid payment information at Expiry Date");
        }


    }



    /**
     * The logic for checkout.
     * Checks for problems like if products are out of stock or payment info is invalid and if so throws error.
     * Otherwise, it creates an Order and adds relevant information to the order such as cost, address, user info.
     * It saves everything, updates the stock and clears the shopping cart.
     * @return the Order
     * @throws NotEnoughProductsInStockException if product is out of stock
     * @throws PaymentFailedException if payment info isn't valid
     */
    public Order processPayment(HttpServletRequest request) throws NotEnoughProductsInStockException, PaymentFailedException {

        //first check if payment info is valid
        checkValidPayment(request);

        //make a new order
        Order order = new Order();


        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            // check if the product is still in stock
            Optional<Product> product = productRepository.findById(entry.getKey().getProductId());
            if (product.isPresent() && product.get().getProductAmountInStock() < entry.getValue()) {
                throw new NotEnoughProductsInStockException(product);
            }
            // update the product stock and add products to the order it to the order
            entry.getKey().setProductAmountInStock(product.get().getProductAmountInStock() - entry.getValue());
            order.addProduct(entry.getKey(), entry.getValue());
        }
        productRepository.saveAll(products.keySet());

        //Set Order info
        order.setUser(userService.getSessionUser());
        Address address = new Address();
        address.setStreetAddress((String) request.getSession().getAttribute("streetAddress"));
        address.setPostalCode((String) request.getSession().getAttribute("postalCode"));
        address.setCity((String) request.getSession().getAttribute("city"));
        address.setFirstName((String) request.getSession().getAttribute("firstName"));
        address.setLastName((String) request.getSession().getAttribute("lastName"));
        address.setPhoneNumber((String) request.getSession().getAttribute("phoneNumber"));
        addressRepository.save(address);
        order.setAddress(address);
        order.setProducts(products.keySet());
        order.setTotalCost(getTotal().add(checkShippingCost(getTotal())).doubleValue());
        orderRepository.save(order);
        products.clear();
        return order;
    }


    /**
     * Returns total cost of items in shopping cart
     * @return BigDecimal of total cost of items
     */

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

    /**
     * Returns a shipping cost that depends on the total cost of the products in the order.
     * If less than 750 returns a shipping cost of 0 and if more than 750 the shipping cost is free (0)
     * @param total the total of the order
     * @return BigDecimal cost of shipping
     */

    public BigDecimal checkShippingCost(BigDecimal total){
        if(total.compareTo(new BigDecimal("750")) < 0){
            return new BigDecimal("99");
        }
        else{
            return new BigDecimal("0");
        }
    }

    /**
     * Returns the amount of products in the cart
     * @return int value of amount of products in the cart
     */


    public int getAmountInCart(){
        return products.values().stream().mapToInt(i -> i).sum();

    }
}