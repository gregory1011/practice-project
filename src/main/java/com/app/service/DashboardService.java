package com.app.service;

import com.app.dto.common.CurrencyDto;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {

    CurrencyDto getCachedCurrencyDto();
    Map<String, BigDecimal> getSummaryNumbers();
}
