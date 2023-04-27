package no.ntnu.crudrest.exception;

public class PaymentFailedException extends Exception {
    public PaymentFailedException(String s) {
        super(s);
    }
}
