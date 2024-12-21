package com.app.service.integration;

import com.app.client.CurrencyExchangeClient;
import com.app.dto.common.CurrencyDto;
import com.app.dto.common.ExchangeRateDto;
import com.app.enums.InvoiceType;
import com.app.service.InvoiceService;
import com.app.service.impl.DashboardServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.awaitility.Awaitility;


import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Log4j2
@SpringBootTest
public class DashboardServiceImpl_IntTest {

    @Mock
    private CurrencyExchangeClient exchangeClient;
    @Mock
    private InvoiceService invoiceService;
    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private ExchangeRateDto exchangeRateDto;
    private CurrencyDto currencyDto;

    @BeforeEach
    void setUp() {
        currencyDto = new CurrencyDto();
        currencyDto.setBritishPound(BigDecimal.valueOf(0.72));
        currencyDto.setCanadianDollar(BigDecimal.valueOf(1.21));
        currencyDto.setIndianRupee(BigDecimal.valueOf(72.93));
        currencyDto.setJapaneseYen(BigDecimal.valueOf(110.27));
        currencyDto.setEuro(BigDecimal.valueOf(120.23));

        exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setUsd(currencyDto);
    }

    @Test
    void testListUsdExchangeRate_Success() {
        //given
        ResponseEntity<ExchangeRateDto> responseEntity = new ResponseEntity<>(exchangeRateDto, HttpStatus.OK);
        //when
        Mockito.when(exchangeClient.getUsdExchangeRate()).thenReturn(responseEntity);
        dashboardService.fetchCurrencyRateAsync();
        //then
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(()-> assertThat(dashboardService.getCachedCurrencyDto())
                        .usingRecursiveComparison()
                        .isEqualTo(currencyDto));
    }

    @Test
    void testListUsdExchangeRate_Failure() {
        //given
        ResponseEntity<ExchangeRateDto> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        //when
        when(exchangeClient.getUsdExchangeRate()).thenReturn(responseEntity);
        dashboardService.fetchCurrencyRateAsync();
        //then
        BigDecimal britishPound = dashboardService.getCachedCurrencyDto().getBritishPound();
        assertThat(britishPound).isZero();
    }

    @Test
    void testGetSummaryNumbers() {
        // given
        BigDecimal totalCost = BigDecimal.valueOf(5000);
        BigDecimal totalSales = BigDecimal.valueOf(10000);
        BigDecimal profitLoss = BigDecimal.valueOf(5000);
        // when
        when(invoiceService.sumTotal(InvoiceType.PURCHASE)).thenReturn(totalCost);
        when(invoiceService.sumTotal(InvoiceType.SALES)).thenReturn(totalSales);
        when(invoiceService.sumProfitLoss()).thenReturn(profitLoss);

        Map<String, BigDecimal> summaryNumbers = dashboardService.getSummaryNumbers();
        // then
        assertThat(summaryNumbers).containsEntry("totalCost", totalCost);
        assertThat(summaryNumbers).containsEntry("totalSales", totalSales);
        assertThat(summaryNumbers).containsEntry("profitLoss", profitLoss);
    }

}
