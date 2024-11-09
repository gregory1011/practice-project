package com.app.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDto {

    @JsonProperty("eur")
    private BigDecimal euro;
    public BigDecimal getEuro() {
        return new BigDecimal(String.format("%.3f", euro));
    }

    @JsonProperty("gbp")
    private BigDecimal britishPound;
    public BigDecimal getBritishPound() {
        return new BigDecimal(String.format("%.3f", britishPound));
    }

    @JsonProperty("jpy")
    private BigDecimal japaneseYen;
    public BigDecimal getJapaneseYen(){
        return new BigDecimal(String.format("%.3f", japaneseYen));
    }

    @JsonProperty("cad")
    private BigDecimal canadianDollar;
    public BigDecimal getCanadianDollar(){
        return new BigDecimal(String.format("%.3f", canadianDollar));
    }

    @JsonProperty("inr")
    private BigDecimal indianRupee;
    public BigDecimal getIndianRupee(){
        return new BigDecimal(String.format("%.3f", indianRupee));
    }
}
