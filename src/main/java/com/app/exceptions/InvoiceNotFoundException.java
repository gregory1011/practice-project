package com.app.exceptions;

public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(String message) {
        super(message);
    }
    public InvoiceNotFoundException() {
        super("Invoice not found");
    }

}
