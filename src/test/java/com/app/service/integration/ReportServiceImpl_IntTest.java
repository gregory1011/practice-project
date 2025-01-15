package com.app.service.integration;

import com.app.dto.InvoiceProductDto;
import com.app.service.ReportService;
import com.app.service.SecuritySetUpUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportServiceImpl_IntTest {


    @Autowired private ReportService reportService;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void test_listAllInvoiceProductsOfCompany() {
        List<InvoiceProductDto> result = reportService.listAllInvoiceProductsOfCompany();
        assertNotNull(result);
        assertThat(result).hasSize(3);
    }

    @Test
    void test_listMonthlyProfitLossMap() {
        Map<String, BigDecimal> map = reportService.listMonthlyProfitLossMap();
        assertNotNull(map);
        assertThat(map).isNotEmpty();
    }
}
