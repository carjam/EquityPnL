package com.companyx.equity.repository;

import com.companyx.equity.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.timestamp < :end ORDER BY t.timestamp")
    List<Transaction> findAllBefore(Date end);

    @Query("SELECT t FROM Transaction t WHERE t.timestamp BETWEEN :start AND :end ORDER BY t.timestamp")
    List<Transaction> findAllBetween(Date start, Date end);
}
