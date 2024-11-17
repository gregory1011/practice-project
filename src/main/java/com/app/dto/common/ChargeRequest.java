package com.app.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;


@Data
public class ChargeRequest {

    private String description;
    private String stripeEmail;
    private String stripeToken;
    private BigDecimal amount;
    private Currency currency;


    @Getter
    @AllArgsConstructor
    public enum Currency {

        EURO("eur"),
        USD("usd"),
        GBP("gbp"),
        JPY("jpy"),
        CAD("cad"),
        INR("inr");

        private final String value;
    }

}
