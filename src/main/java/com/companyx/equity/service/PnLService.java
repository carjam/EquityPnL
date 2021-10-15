package com.companyx.equity.service;

import com.companyx.equity.model.Transaction;
import com.companyx.equity.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PnLService {
    private final String CASH = "cash";

    @Autowired
    TransactionRepository transactionRepository;

    //Position endPos = start positions + buys - sales;
    public Map getPosition(Date start, Date end) {
        Map<String, Pair<BigDecimal, BigInteger>> positions = new HashMap<>(); //(value, quantity) tuple

        List<Transaction> priorTrans = transactionRepository.findAllBefore(start);
        for(Transaction transaction : priorTrans) {
            String sym = transaction.getSymbol();

            Pair<BigDecimal, BigInteger> startPos = Pair.of(BigDecimal.ZERO, BigInteger.ZERO); //(value, quantity) tuple
            switch(transaction.getTransactionType().getDescription()) {
                case Transaction.DEPOSIT:
                    sym = CASH;
                    if(positions.containsKey(sym))
                        startPos = positions.get(sym);
                    positions.put(sym,  Pair.of(startPos.getLeft().add(transaction.getValue()), BigInteger.ZERO));
                    break;
                case Transaction.WITHDRAWAL:
                    sym = CASH;
                    if(positions.containsKey(sym))
                        startPos = positions.get(sym);
                    positions.put(sym,  Pair.of(startPos.getLeft().subtract(transaction.getValue()), BigInteger.ZERO));
                    break;
                case Transaction.BUY:
                    if(positions.containsKey(sym))
                        startPos = positions.get(sym);


                    positions.put(sym,  Pair.of(startPos.getLeft().subtract(transaction.getValue()), startPos.getRight().add(transaction.getQuantity())));
                    positions.put(CASH,  Pair.of(startPos.getLeft().add(transaction.getValue()), BigInteger.ZERO));
                    break;
                case Transaction.SALE:
                    if(positions.containsKey(sym))
                        startPos = positions.get(sym);


                    positions.put(sym,  Pair.of(startPos.getLeft().add(transaction.getValue()), startPos.getRight().subtract(transaction.getQuantity())));
                    positions.put(CASH,  Pair.of(startPos.getLeft().add(transaction.getValue()), BigInteger.ZERO));
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown transaction type " + transaction.getTransactionType().getDescription());
            }
        }
        log.info(new Timestamp(System.currentTimeMillis()) + " "
                + this.getClass() + ":"
                + new Throwable().getStackTrace()[0].getMethodName()
                + "\nStart Position: " + positions
        );

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
