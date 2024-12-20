package com.app.service.integration;


import com.app.service.DashboardService;
import com.app.service.InvoiceService;
import com.app.service.SecuritySetUpUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DashboardServiceImpl_IntTest {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }
}
