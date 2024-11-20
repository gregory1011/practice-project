package com.app.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyStatus {

    ACTIVE("Active"),
    PASSIVE("Passive");

    private final String value;

}
