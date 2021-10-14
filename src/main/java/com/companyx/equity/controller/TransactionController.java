package com.companyx.equity.controller;

import com.companyx.equity.model.Transaction;
import com.companyx.equity.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@RestController
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/Transaction")
    public List<Transaction> index(){
        return transactionRepository.findAll();
    }

    @GetMapping("/Transaction/{id}")
    public Transaction show(@PathVariable String id){
        Integer transactionId = Integer.parseInt(id);
        return transactionRepository.findById(transactionId).get();
    }
}