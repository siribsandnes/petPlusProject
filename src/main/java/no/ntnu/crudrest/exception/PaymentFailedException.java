package no.ntnu.crudrest.exception;
/**
 * Exception for when payment info is invalid.
 */
public class PaymentFailedException extends Exception {
    public PaymentFailedException(String s) {
        super(s);
    }
}
