package com.app.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;
    private Integer year;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private boolean isPaid;
    private String companyStripeId;
    private Month month;
    private String description;
    private CompanyDto company;
}
