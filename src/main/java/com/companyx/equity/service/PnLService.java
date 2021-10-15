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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PnLService {
    private final String CASH = "cash";

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    FinhubRepository finhubRepository;

    //Position endPos = start positions + buys - sales;
    public Map<String, Pair<BigDecimal, BigInteger>> getPosition(Date start, Date end) {
        Map<String, Pair<BigDecimal, BigInteger>> positions = new HashMap<>(); //(value, quantity) tuple

        List<Transaction> priorTrans = transactionRepository.findAllBefore(start);
        log.info(new Timestamp(System.currentTimeMillis()) + " "
                + this.getClass() + ":"
                + new Throwable().getStackTrace()[0].getMethodName()
                + "\n" + priorTrans.size() + " transactions: " + priorTrans
                + "\n" + " from " + start + " to " + end
        );
        for(Transaction transaction : priorTrans) {
            String sym = transaction.getSymbol();
            if(TransactionType.CASH_TRANS.contains(transaction.getTransactionType().getDescription()))
                sym = CASH;

            Pair<BigDecimal, BigInteger> startPos = Pair.of(BigDecimal.ZERO, BigInteger.ZERO); //(value, quantity) tuple
            if(positions.containsKey(sym))
                startPos = positions.get(sym);

            switch(transaction.getTransactionType().getDescription()) {
                case TransactionType.DEPOSIT:
                    positions.put(sym, Pair.of(startPos.getLeft().add(transaction.getValue()), BigInteger.ZERO));
                    break;
                case TransactionType.WITHDRAWAL:
                    positions.put(sym, Pair.of(startPos.getLeft().subtract(transaction.getValue()), BigInteger.ZERO));
                    break;
                case TransactionType.BUY:

                    positions.put(sym, Pair.of(startPos.getLeft().subtract(transaction.getValue()), startPos.getRight().add(transaction.getQuantity())));
                    positions.put(CASH, Pair.of(positions.get(CASH).getLeft().subtract(transaction.getValue()), BigInteger.ZERO));
                    break;
                case TransactionType.SALE:

                    positions.put(sym, Pair.of(startPos.getLeft().add(transaction.getValue()), startPos.getRight().subtract(transaction.getQuantity())));
                    positions.put(CASH, Pair.of(positions.get(CASH).getLeft().add(transaction.getValue()), BigInteger.ZERO));
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown transaction type " + transaction.getTransactionType().getDescription());
            }
            log.info(new Timestamp(System.currentTimeMillis()) + " "
                    + this.getClass() + ":"
                    + new Throwable().getStackTrace()[0].getMethodName()
                    + "\ntransaction: " + transaction.toString()
                    + "\npositions: " + positions
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

    /*
    for trans, if BUY price *= -1

     */

    /*private void getRealized(DateTime start, DateTime end) {
        //start basis - end basis = loss
        //loss * shares sold/shares held(start) = realized
        //loss * shares held(end)/shares held(start) = unrealized
    }

    private void getUnRealized(DateTime start, DateTime end) {
        //Position endPos = getPosition(start, end);
        //iterate over endPos, calling out for price @end (candles if < today, else mark)
        // unrealized = (endPrice * endPos) - (historical net value(price * amt))
        //return endPos
    }
     */
}
