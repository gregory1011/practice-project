package com.app.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;
    @NotBlank(message = "Address is a required field.")
    @Size(min= 2, max = 50, message = "Address should have 2-50 characters long.")
    private String addressLine1;

    @NotBlank(message = "Address is a required field.")
    @Size(min= 2, max = 50, message = "Address should have 2-50 characters long.")
    private String addressLine2;

    @NotBlank(message = "City is a required field.")
    @Size(min= 2, max = 25, message = "City should have 2-25 characters long.")
    private String city;

    @NotBlank(message = "State is a required field.")
    @Size(min= 2, max = 25, message = "State should have 2-50 characters long.")
    private String state;

    @NotBlank(message = "Country is a required field.")
    private String country;

    @NotBlank(message = "Zip Code is a required field.")
    @Pattern(regexp = "^\\d{5}(?:-\\d{4})?$",  message = "Zipcode should have a valid form 'XXXXX' or 'XXXXX-XXXX'.")
    private String zipCode;

}
