package com.app.client;

import com.app.dto.common.ExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "${api.currency.ulr}", name = "CurrencyExchange-Client")
public interface CurrencyExchangeClient {

    @GetMapping("/usd.json")
    ResponseEntity<ExchangeRateDto> getUsdExchangeRate();
}
