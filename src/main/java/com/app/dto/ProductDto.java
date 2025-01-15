package com.app.dto;


import com.app.enums.ProductUnit;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name is a required field.")
    private String name;

    private Integer quantityInStock;

    @Positive
    private Integer lowLimitAlert;

    @NotNull(message = "Product unit is a required field.")
    private ProductUnit productUnit;

    @NotNull(message = "Category is a required field.")
    private CategoryDto category;

    private boolean hasInvoiceProduct;

}
