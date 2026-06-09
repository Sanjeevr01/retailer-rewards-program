package com.sanjeev.rewards.util;

import com.sanjeev.rewards.entity.Transaction;
import com.sanjeev.rewards.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final TransactionRepository transactionRepository;

    public DataLoader(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {

        transactionRepository.save(
                new Transaction(
                        null,
                        1L,
                        "John",
                        120.0,
                        LocalDate.now().minusMonths(2)
                )
        );

        transactionRepository.save(
                new Transaction(
                        null,
                        1L,
                        "John",
                        75.0,
                        LocalDate.now().minusMonths(1)
                )
        );

        transactionRepository.save(
                new Transaction(
                        null,
                        2L,
                        "David",
                        200.0,
                        LocalDate.now().minusMonths(1)
                )
        );

        transactionRepository.save(
                new Transaction(
                        null,
                        2L,
                        "David",
                        95.0,
                        LocalDate.now()
                )
        );

        transactionRepository.save(
                new Transaction(
                        null,
                        3L,
                        "Smith",
                        45.0,
                        LocalDate.now().minusMonths(1)
                )
        );
    }
}
