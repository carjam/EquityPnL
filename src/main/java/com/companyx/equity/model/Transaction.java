package com.companyx.equity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    //User
    //TransactionType
    private String symbol;
    private Float quantity;
    private BigDecimal value;
    private Timestamp timestamp;

    //TODO: translate Finhub integer seconds from epoc to Timestamp

    @Override
    public String toString() {
        return "";
    }
}