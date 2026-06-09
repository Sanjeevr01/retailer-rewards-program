package com.sanjeev.rewards.repository;

import com.sanjeev.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTransactionDateAfter(LocalDate date);

    List<Transaction> findByCustomerIdAndTransactionDateAfter(
            Long customerId,
            LocalDate date
    );

}
