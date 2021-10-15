package com.companyx.equity.controller;

import com.companyx.equity.model.Transaction;
import com.companyx.equity.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/Transaction/{id}")
    public Transaction show(@PathVariable String id){
        Integer transactionId = Integer.parseInt(id);
        return transactionRepository.findById(transactionId).get();
    }

    @GetMapping("/Transaction")
    public List<Transaction> findBetween(@RequestParam Optional<String> from, @RequestParam Optional<String> to) throws ParseException {
        Date fromDate = null;
        Date toDate = null;
        if (from.isPresent()) {
            fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from.get());
        }
        if (to.isPresent()) {
            toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to.get());
        }

        if (Objects.isNull(fromDate) && Objects.isNull(toDate))
            return transactionRepository.findAll();
        else if (Objects.isNull(fromDate))
            return transactionRepository.findAllBefore(toDate);
        else
            return transactionRepository.findAllBetween(fromDate, toDate);
    }
}