package com.companyx.equity.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    //User
    //TransactionType
    private String symbol;
    private float quantity;
    private BigDecimal value;
    private Timestamp timestamp;

    @Override
    public String toString() {
        return "";
    }
}