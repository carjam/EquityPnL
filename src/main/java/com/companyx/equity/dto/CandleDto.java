package com.companyx.equity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

// https://finnhub.io/docs/api/stock-candles
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown =  true)
public class CandleDto {
    public final String OK = "ok";
    public final String NO_DATA = "no_data";

    @JsonProperty("c")
    private List<BigDecimal> close;
    @JsonProperty("h")
    private List<BigDecimal> hi;
    @JsonProperty("l")
    private List<BigDecimal> lo;
    @JsonProperty("o")
    private List<BigDecimal> open;
    @JsonProperty("s")
    private String status;
    @JsonProperty("t")
    private List<BigInteger> timestamp;
    @JsonProperty("v")
    private List<BigInteger> volume;
}