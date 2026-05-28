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
                        LocalDate.of(2026, 3, 15)
                )
        );

        transactionRepository.save(
                new Transaction(
                        null,
                        1L,
                        "John",
                        75.0,
                        LocalDate.of(2026, 4, 10)
                )
        );

        transactionRepository.save(
                new Transaction(
                        null,
                        2L,
                        "David",
                        200.0,
                        LocalDate.of(2026, 3, 5)
                )
        );

        transactionRepository.save(
                new Transaction(
                        null,
                        2L,
                        "David",
                        95.0,
                        LocalDate.of(2026, 5, 20)
                )
        );

        transactionRepository.save(
                new Transaction(
                        null,
                        3L,
                        "Smith",
                        45.0,
                        LocalDate.of(2026, 4, 25)
                )
        );
    }
}
