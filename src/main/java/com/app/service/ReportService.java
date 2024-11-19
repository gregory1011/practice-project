package com.app.service;

import com.app.dto.InvoiceProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportService {

    List<InvoiceProductDto> listAllInvoiceProductsOfCompany();
    Map<String, BigDecimal> listMonthlyProfitLossMap();
}
