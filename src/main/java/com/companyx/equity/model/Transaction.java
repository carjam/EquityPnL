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
    public static final String DEPOSIT = "deposit";
    public static final String WITHDRAWAL = "withdraw";
    public static final String BUY = "buy";
    public static final String SALE = "sell";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String symbol;
    private BigInteger quantity;
    private BigDecimal value;
    private Timestamp timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private TransactionType transactionType;
}