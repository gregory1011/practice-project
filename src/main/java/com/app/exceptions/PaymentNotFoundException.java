package com.app.exceptions;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String message) {
        super(message);
    }
    public PaymentNotFoundException() {
        super("Payment not found");
    }

}
