package com.app.exceptions;

public class InvoiceProductNotFoundException extends RuntimeException {

    public InvoiceProductNotFoundException(String message) {
        super(message);
    }
    public InvoiceProductNotFoundException() {
        super("Invoice product not found");
    }

}
