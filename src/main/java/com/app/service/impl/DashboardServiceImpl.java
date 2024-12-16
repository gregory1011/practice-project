package com.app.service.impl;

import com.app.annotation.ExecutionTime;
import com.app.client.CurrencyExchangeClient;
import com.app.dto.common.CurrencyDto;
import com.app.dto.common.ExchangeRateDto;
import com.app.enums.InvoiceType;
import com.app.service.DashboardService;
import com.app.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceService invoiceService;
    private final CurrencyExchangeClient currencyExchange;

    private  CurrencyDto cachedCurrencyDto;

    @Scheduled(fixedRate = 10_000) // every 100 seconds from milliseconds
    public void scheduleFetchCurrencyRate(){
        fetchCurrencyRateAsync();
    }

    @Async
    public void fetchCurrencyRateAsync() {
        log.info("... fetching currencies with thread : {}", Thread.currentThread().getName());
        try {
            ResponseEntity<ExchangeRateDto> response = currencyExchange.getUsdExchangeRate();
            if (response.getStatusCode().is2xxSuccessful()) cachedCurrencyDto= Objects.requireNonNull(response.getBody()).getUsd();
            else log.warn("failed to fetch and cache currency... : {}", response.getStatusCode());
        }catch (Exception e) {
            log.error("...failed to fetch and cache currency... : {}", e.getMessage());
        }
    }

    @ExecutionTime
    @Override
    public CurrencyDto getCachedCurrencyDto() {
        if (cachedCurrencyDto == null){
            log.error("While requesting rates, cached currency was null, trying to fetch again...");
            fetchCurrencyRateAsync();
            if(cachedCurrencyDto == null){
                CurrencyDto currencyDto = new CurrencyDto();
                currencyDto.setEuro(BigDecimal.ZERO);
                currencyDto.setBritishPound(BigDecimal.ZERO);
                currencyDto.setCanadianDollar(BigDecimal.ZERO);
                currencyDto.setJapaneseYen(BigDecimal.ZERO);
                currencyDto.setIndianRupee(BigDecimal.ZERO);
                cachedCurrencyDto= currencyDto;
            }
        }
        return cachedCurrencyDto;
    }

    @Override
    public Map<String, BigDecimal> getSummaryNumbers() {
        Map<String, BigDecimal> summaryNumbers = new HashMap<>();
        BigDecimal totalCost = invoiceService.sumTotal(InvoiceType.PURCHASE);
        BigDecimal totalSales = invoiceService.sumTotal(InvoiceType.SALES);
        BigDecimal profitLoss = invoiceService.sumProfitLoss();
//        add to Map
        summaryNumbers.put("totalCost", totalCost);
        summaryNumbers.put("totalSales", totalSales);
        summaryNumbers.put("profitLoss", profitLoss);
        return summaryNumbers;
    }

}
