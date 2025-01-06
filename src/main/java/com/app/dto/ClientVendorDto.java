package com.app.dto;

import com.app.enums.ClientVendorType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientVendorDto {

    private Long id;

    @NotBlank(message = "Name is required field.")
    @Size(min = 2, max = 50, message = "Client/Vendor aame must be between 2 and 50 characters long.")
    private String clientVendorName;

    @NotBlank(message = "Website is required field.")
    @Pattern(regexp = "^(https?://)?([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,})(:[0-9]+)?(/[^/]+)*$", message = "enter a valid website format.")
    private String website;

    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125  +1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"                                  // +111 123 456 789
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;

    @NotNull(message = "Please select type.")
    private ClientVendorType clientVendorType;

    @Valid
    private AddressDto address;

    private CompanyDto company;
    private boolean hasInvoice;
}
