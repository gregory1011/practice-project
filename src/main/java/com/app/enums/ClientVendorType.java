package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientVendorType {

    VENDOR("Vendor"),
    CLIENT("Client");

    private final String value;
}
