package com.app.service.impl;

import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import com.app.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final InvoiceProductService invoiceProductService;
    private final InvoiceService invoiceService;

    @Override
    public List<InvoiceProductDto> listAllInvoiceProductsOfCompany() {
        return invoiceProductService.listAllApprovedInvoiceProductsOfCompany();
    }

    @Override
    public Map<String, BigDecimal> listMonthlyProfitLossMap() {
        Map<String, BigDecimal> monthlyProfitLoss = new LinkedHashMap<>();
        List<InvoiceProductDto> list = invoiceService.listInvoices(InvoiceType.SALES).stream()
                .filter(m -> m.getInvoiceStatus().equals(InvoiceStatus.APPROVED))
                .sorted(Comparator.comparing(InvoiceDto::getDate).reversed())
                .flatMap(each -> invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(each.getId())
                        .stream())
                .toList();

        list.forEach(each ->{
            String key = each.getInvoice().getDate().getMonth() + " - " + each.getInvoice().getDate().getYear();
            BigDecimal profitLoss = each.getProfitLoss();
            monthlyProfitLoss.put(key, profitLoss);
        });

        return monthlyProfitLoss;
    }

}
