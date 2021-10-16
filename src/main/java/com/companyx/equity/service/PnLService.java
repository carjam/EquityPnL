package com.companyx.equity.service;

import com.companyx.equity.model.Transaction;
import com.companyx.equity.model.TransactionType;
import com.companyx.equity.repository.FinhubRepository;
import com.companyx.equity.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PnLService {
    private final String CASH = "cash";
    private final int ROUNDING_SCALE = 6;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    FinhubRepository finhubRepository;

    //Position endPos = start positions + buys - sales;
    public Map<String, Pair<BigDecimal, BigInteger>> getPosition(Date start, Date end) {
        Map<String, Pair<BigDecimal, BigInteger>> positions = new HashMap<>(); //(basis, quantity) tuple

        List<Transaction> priorTrans = transactionRepository.findAllBefore(start);
        log.info(new Timestamp(System.currentTimeMillis()) + " "
                + this.getClass() + ":"
                + new Throwable().getStackTrace()[0].getMethodName()
                + "\n" + priorTrans.size() + " transactions"
                + "\n" + " from " + start + " to " + end
        );

        //get the starting (basis, quantity)
        // long = negative value, positive quant
        // short = positive value, negative quant
        for(Transaction transaction : priorTrans) {
            String sym = transaction.getSymbol();
            if(TransactionType.CASH_TRANS.contains(transaction.getTransactionType().getDescription()))
                sym = CASH;

            BigDecimal transPrice, transVal, startPrice, startVal, endVal, cash;
            BigInteger startQuant, transQuant, endQuant;

            Pair<BigDecimal, BigInteger> startPos = Pair.of(BigDecimal.ZERO, BigInteger.ZERO); //(basis, quantity) tuple
            if(positions.containsKey(sym))
                startPos = positions.get(sym);
            startVal = startPos.getLeft();
            startQuant = startPos.getRight();
            startPrice = startPos.getRight().equals(BigInteger.ZERO) ? BigDecimal.ZERO
                    : startVal.divide(new BigDecimal(startQuant), ROUNDING_SCALE, RoundingMode.HALF_UP).abs();
            cash = transaction.getValue();

            switch(transaction.getTransactionType().getDescription()) {
                case TransactionType.DEPOSIT:
                    positions.put(sym, Pair.of(startPos.getLeft().add(cash), BigInteger.ZERO));
                    break;
                case TransactionType.WITHDRAWAL:
                    positions.put(sym, Pair.of(startPos.getLeft().subtract(cash), BigInteger.ZERO));
                    break;
                case TransactionType.BUY:
                    //trans inputs always >= 0
                    transVal = transaction.getValue();
                    transQuant = transaction.getQuantity();
                    transPrice = transQuant.equals(BigInteger.ZERO) ? BigDecimal.ZERO
                            : transVal.divide(new BigDecimal(transQuant), ROUNDING_SCALE, RoundingMode.HALF_UP);

                    endQuant = startQuant.add(transQuant);

                    //long -> long
                    if((endQuant.compareTo(BigInteger.ZERO) > 0) && (startQuant.compareTo(BigInteger.ZERO) > 0))
                        endVal = startVal.subtract(transVal);
                    // short -> short
                    else if((endQuant.compareTo(BigInteger.ZERO) < 0) && (startQuant.compareTo(BigInteger.ZERO) < 0))
                        endVal = startPrice.multiply(new BigDecimal(endQuant)).multiply(new BigDecimal(-1));
                    //long -> short | short -> long
                    else
                        endVal = transPrice.multiply(new BigDecimal(endQuant)).multiply(new BigDecimal(-1));
                    positions.put(sym, Pair.of(endVal, endQuant));
                    positions.put(CASH, Pair.of(positions.get(CASH).getLeft().subtract(cash), BigInteger.ZERO));
                    break;
                case TransactionType.SALE:
                    //trans inputs always >= 0
                    transVal = transaction.getValue();
                    transQuant = transaction.getQuantity();
                    transPrice = transQuant.equals(BigInteger.ZERO) ? BigDecimal.ZERO
                            : transVal.divide(new BigDecimal(transQuant), ROUNDING_SCALE, RoundingMode.HALF_UP);

                    endQuant = startQuant.subtract(transaction.getQuantity());

                    //long -> long
                    if((endQuant.compareTo(BigInteger.ZERO) > 0) && (startQuant.compareTo(BigInteger.ZERO) > 0))
                        endVal = startPrice.multiply(new BigDecimal(endQuant)).multiply(new BigDecimal(-1));
                    //short -> short
                    else if((endQuant.compareTo(BigInteger.ZERO) < 0) && (startQuant.compareTo(BigInteger.ZERO) < 0))
                        endVal = startVal.add(transVal);
                    //long -> short | short -> long
                    else
                        endVal = transPrice.multiply(new BigDecimal(endQuant)).multiply(new BigDecimal(-1));
                    positions.put(sym, Pair.of(endVal, endQuant));
                    positions.put(CASH, Pair.of(positions.get(CASH).getLeft().add(cash), BigInteger.ZERO));
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown transaction type " + transaction.getTransactionType().getDescription());
            }
            log.info(new Timestamp(System.currentTimeMillis()) + " "
                    + this.getClass() + ":"
                    + new Throwable().getStackTrace()[0].getMethodName()
                    + "\n### End transaction: " + transaction.toString()
                    + "\n### End positions: " + positions
            );
        }
        log.info(new Timestamp(System.currentTimeMillis()) + " "
                + this.getClass() + ":"
                + new Throwable().getStackTrace()[0].getMethodName()
                + "\nStart Position: " + positions
        );
        return positions;
        /*
        //get buys & sales
        List<Transaction> transactions = transactionRepository.findAllBetween(start, end);

        //update positions based on buys and sales
        for(Transaction transaction : transactions) {
            String sym = transaction.getSymbol();

            Pair<BigDecimal, BigInteger> startPos = Pair.of(BigDecimal.ZERO, BigInteger.ZERO); //(value, quantity) tuple
            if(positions.containsKey(sym))
                startPos = positions.get(sym);
            positions.put(sym,  Pair.of(startPos.getLeft().add(transaction.getValue()), startPos.getRight().add(transaction.getQuantity())));
        }
        log.info(new Timestamp(System.currentTimeMillis()) + " "
                + this.getClass() + ":"
                + new Throwable().getStackTrace()[0].getMethodName()
                + "\nEnd Position: " + positions
        );

        return positions;
         */
    }

    public Transaction getTransactionById(String id){
        Integer transactionId = Integer.parseInt(id);
        return transactionRepository.findById(transactionId).get();
    }

    public List<Transaction> getTransactionByDates(Optional<String> from, Optional<String> to) throws ParseException {
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
