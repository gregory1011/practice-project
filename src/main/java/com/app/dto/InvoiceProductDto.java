package com.app.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {

    private Long id;

    @NotNull(message = "Quantity is a required field")
    @Max(value = 100, message = "Quantity should not be greater than 100")
    @Positive
    private Integer quantity;

    @NotNull(message = "Price is a required field.")
    @Positive
    private BigDecimal price;

    @NotNull(message = "Tax is a required field.")
    @Min(value = 0, message = "Tax should be at least 0 %")
    @Max(value = 20, message = " Tax should not be greater than 20 %")
    private Integer tax;

    private BigDecimal total;

    private BigDecimal profitLoss;

    private Integer remainingQuantity;

    private InvoiceDto invoice;

    @NotNull(message = "Product is required field")
    private ProductDto product;

}
