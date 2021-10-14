package com.companyx.equity.service;

import com.companyx.equity.model.Transaction;
import com.companyx.equity.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PnLService {
    @Autowired
    TransactionRepository transactionRepository;

    //Position endPos = positions + buys - sales;
    private Map getPosition(Date start, Date end) {
        // get postions at start
        Map<String, BigDecimal> positions = new HashMap<>();
        List<Transaction> priorTrans = transactionRepository.findAllBefore(start);
        //symbol -> (price * amt), netted chronologically
        for(Transaction transaction : priorTrans) {
            String sym = transaction.getSymbol();
            BigDecimal startVal = new BigDecimal(0);
            if(positions.containsKey(sym))
                startVal = positions.get(sym);
            positions.put(sym,  startVal.add(transaction.getValue()));
        }

        //get buys & sales
        List<Transaction> transactions = transactionRepository.findAllBetween(start, end);

        //update positions based on buys and sales
        for(Transaction transaction : transactions) {
            String sym = transaction.getSymbol();
            BigDecimal startVal = new BigDecimal(0);
            if(positions.containsKey(sym))
                startVal = positions.get(sym);
            positions.put(sym,  startVal.add(transaction.getValue()));
        }
        return positions;
    }

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
