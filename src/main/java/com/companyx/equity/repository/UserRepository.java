package com.companyx.equity.repository;


import com.companyx.equity.model.Equity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Equity, Integer> {
}
