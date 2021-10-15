package com.companyx.equity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

// https://finnhub.io/docs/api/quote
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown =  true)
public class MarkDto {
    @JsonProperty("c")
    private BigDecimal currentPrice;
    @JsonProperty("d")
    private BigDecimal change;
    @JsonProperty("dp")
    private float precentChange;
    @JsonProperty("h")
    private BigDecimal dailyHi;
    @JsonProperty("l")
    private BigDecimal dailyLo;
    @JsonProperty("o")
    private BigDecimal open;
    @JsonProperty("pc")
    private BigDecimal priorClose;
    @JsonProperty("t")
    private BigInteger timestamp;
}