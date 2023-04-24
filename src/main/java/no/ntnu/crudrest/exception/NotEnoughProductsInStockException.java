package no.ntnu.crudrest.exception;

import no.ntnu.crudrest.models.Product;

import java.util.Optional;

/**
 * Exception for checkout when item is not in stock.
 */
public class NotEnoughProductsInStockException extends Exception {

    private static final String DEFAULT_MESSAGE = "Not enough products in stock";

    public NotEnoughProductsInStockException() {
        super(DEFAULT_MESSAGE);
    }

    public NotEnoughProductsInStockException(Optional<Product> product) {
        super(String.format("Not enough %s products in stock. Only %d left", product.get().getProductName(), product.get().getProductAmountInStock()));
    }

}
