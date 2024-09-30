package com.app.dto;


import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String description;
    private CompanyDto company;
    private boolean hasProduct;
}
