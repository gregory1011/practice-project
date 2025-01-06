package com.app.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Description is a required field.")
    @Size(min = 2, max = 100, message = "Description must be between 2 and 100 characters long.")
    private String description;

    private CompanyDto company;

    private boolean hasProduct;
}
