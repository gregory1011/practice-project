package com.app.exceptions;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(String message) {
        super(message);
    }
    public CompanyNotFoundException() {
        super("Company not found");
    }

}
