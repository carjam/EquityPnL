package com.companyx.equity.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

// https://finnhub.io/docs/api/quote
public class MarkDto {
    private BigDecimal currentPrice;
    private BigDecimal change;
    private float precentChange;
    private BigDecimal dailyHi;
    private BigDecimal dailyLo;
    private BigDecimal open;
    private BigDecimal priorClose;
    private BigInteger timestamp;

    @Override
    public String toString() {
        return "";
    }
}