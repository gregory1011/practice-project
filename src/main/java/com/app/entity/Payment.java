package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.Months;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "payments")
public class Payment extends BaseEntity {

    private Integer year;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private boolean isPaid;

    private String companyStripeId;

    @Enumerated(EnumType.STRING)
    private Months month;

    @ManyToOne
    private Company company;
}
