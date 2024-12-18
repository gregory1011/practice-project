package com.app.exceptions;

public class ProductLowLimitAlertException extends RuntimeException {

    public ProductLowLimitAlertException(String m){
        super(m);
    }
    public ProductLowLimitAlertException(){
        super("This stock decrease below line limit.");
    }
}
