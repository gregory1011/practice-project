package com.app.exceptions;

public class ClientVendorNotFoundException extends RuntimeException {

    public ClientVendorNotFoundException(String message) {
        super(message);
    }
    public ClientVendorNotFoundException() {
        super("Client/Vendor could not be found");
    }

}
