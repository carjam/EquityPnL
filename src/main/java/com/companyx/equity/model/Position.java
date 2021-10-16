package com.companyx.equity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Position {
    public Position(Timestamp timestamp, String symbol) {
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.quantity = BigInteger.ZERO;
        this.value = BigDecimal.ZERO;
        this.realizedITD = BigDecimal.ZERO;
        this.unrealized = BigDecimal.ZERO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Timestamp timestamp;
    private String symbol;
    private BigInteger quantity;
    private BigDecimal value;
    private BigDecimal realizedITD;
    private BigDecimal unrealized;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Override
    public String toString() {
        return "{" +
            "\"timestamp\":" + timestamp +
            "\"symbol\":" + symbol +
            "\"quantity\":" + quantity +
            "\"value\":" + value +
        "}";
    }
}