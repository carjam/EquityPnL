package com.companyx.equity.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

// https://finnhub.io/docs/api/stock-candles
public class CandleDto {
    public final String OK = "ok";
    public final String NO_DATA = "no_data";

    private List<BigDecimal> close;
    private List<BigDecimal> hi;
    private List<BigDecimal> lo;
    private List<BigDecimal> open;
    private String status;
    private BigInteger timestamp;
    private List<BigInteger> volume;

    @Override
    public String toString() {
        return "";
    }
}