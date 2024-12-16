package com.app.service.unit;


import com.app.client.CurrencyExchangeClient;
import com.app.dto.common.CurrencyDto;
import com.app.dto.common.ExchangeRateDto;
import com.app.service.InvoiceService;
import com.app.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class DashboardServiceImpl_UnitTest {

    @Mock
    private InvoiceService invoiceService;
    @Mock
    private CurrencyExchangeClient currencyExchangeClient;
    @InjectMocks
    private DashboardServiceImpl dashboardService;


    private CurrencyDto currencyDto;
    private ExchangeRateDto exchangeRateDto;

    @BeforeEach
    void setUp() {
        currencyDto = new CurrencyDto();
        currencyDto.setIndianRupee(new BigDecimal("1.25"));
        currencyDto.setBritishPound(new BigDecimal("1.50"));
        currencyDto.setCanadianDollar(new BigDecimal("1.14"));
        currencyDto.setJapaneseYen(new BigDecimal("1.44"));
        currencyDto.setEuro(new BigDecimal("1.09"));

        exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setUsd(currencyDto);
    }


    @Test
    void test() {
//        ResponseEntity<ExchangeRateDto> actualResult= new ResponseEntity<>(exchangeRateDto, HttpStatus.OK);
//        when(currencyExchangeClient.getUsdExchangeRate()).thenReturn(actualResult);
//
//        dashboardService.fetchCurrencyRateAsync();

    }
}
