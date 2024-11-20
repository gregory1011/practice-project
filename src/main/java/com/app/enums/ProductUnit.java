package com.app.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductUnit {

    LBS("Libre"),
    GALLON("Gallon"),
    PCS("Pieces"),
    KG("Kilogram"),
    METER("Meter"),
    INCH("Inch"),
    FEET("Feet");

    private final String value;

}
