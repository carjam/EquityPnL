package com.companyx.equity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquityRepository extends JpaRepository<Equity, Integer> {

    List<Equity> findByNameContaining(String text);
}
