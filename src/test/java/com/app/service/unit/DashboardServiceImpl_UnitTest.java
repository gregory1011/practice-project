package com.app.service.unit;


import com.app.client.CurrencyExchangeClient;
import com.app.dto.common.CurrencyDto;
import com.app.dto.common.ExchangeRateDto;
import com.app.service.InvoiceService;
import com.app.service.impl.DashboardServiceImpl;
import lombok.extern.log4j.Log4j2;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@Log4j2
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
    void testFetchCurrencyRatesAsync_Successfully() {
        //given
//        ExchangeRateDto exchangeRateDto= new ExchangeRateDto();
//        exchangeRateDto.setUsd(currencyDto);

        ResponseEntity<ExchangeRateDto> responseEntity= new ResponseEntity<>(exchangeRateDto, HttpStatus.OK);
        //when
        when(currencyExchangeClient.getUsdExchangeRate()).thenReturn(responseEntity);
        dashboardService.fetchCurrencyRateAsync();
        //then
        assertThat(dashboardService.getCachedCurrencyDto()).isEqualTo(currencyDto);
        verify(currencyExchangeClient, times(1)).getUsdExchangeRate();
    }
    @Test
    void testFetchCurrencyRateAsync_Failure() {
        ResponseEntity<ExchangeRateDto> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        //when
        when(currencyExchangeClient.getUsdExchangeRate()).thenReturn(responseEntity);
        dashboardService.fetchCurrencyRateAsync();
        //then
        assertThat(dashboardService.getCachedCurrencyDto()).isNotEqualTo(currencyDto);
        verify(currencyExchangeClient, times(2)).getUsdExchangeRate();
    }

    @Test
    void testGetCachedCurrencyDto_FullBack() {
        CurrencyDto fallBackCurrencyDto = dashboardService.getCachedCurrencyDto();
        assertThat(fallBackCurrencyDto.getBritishPound()).isZero();
        assertThat(fallBackCurrencyDto.getCanadianDollar()).isZero();
        assertThat(fallBackCurrencyDto.getIndianRupee()).isZero();
        assertThat(fallBackCurrencyDto.getJapaneseYen()).isZero();
        assertThat(fallBackCurrencyDto.getEuro()).isZero();
    }

    @Test
    void testGetCachedCurrencyDto_whenCacheIsNull_fetchCurrencyRate() throws ExecutionException, InterruptedException {
        //given
        ResponseEntity<ExchangeRateDto> responseEntity = new ResponseEntity<>(exchangeRateDto, HttpStatus.OK);
        //when
        when(currencyExchangeClient.getUsdExchangeRate()).thenReturn(responseEntity);
        CurrencyDto result = dashboardService.getCachedCurrencyDto();

        CompletableFuture.runAsync(()->{
            while (dashboardService.getCachedCurrencyDto()==null){
                try {
                    Thread.sleep(100);
                }catch (InterruptedException ex){
                    Thread.currentThread().interrupt();
                }
            }
        }).get();
        //verify the fetchCurrencyAsync was called
        verify(currencyExchangeClient).getUsdExchangeRate();
        //verify the result after async fetch
        assertThat(dashboardService.getCachedCurrencyDto()).isEqualTo(currencyDto);
    }

    @Test
    void testGetSummaryNumbers() {

    }

}
