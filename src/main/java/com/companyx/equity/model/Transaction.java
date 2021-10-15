package com.companyx.equity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Timestamp timestamp;
    private String symbol;
    private BigInteger quantity;
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private TransactionType transactionType;

    @Override
    public String toString() {
        return "{" +
            "\"timestamp\":" + timestamp +
            "\"symbol\":" + symbol +
            "\"type\":" + transactionType.getDescription() +
            "\"quantity\":" + quantity +
            "\"value\":" + value +
        "}";
    }
}