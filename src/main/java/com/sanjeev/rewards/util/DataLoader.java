package com.sanjeev.rewards.util;

import com.sanjeev.rewards.entity.Transaction;
import com.sanjeev.rewards.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final TransactionRepository transactionRepository;

    public DataLoader(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {

        transactionRepository.saveAll(List.of(

                // Customer 1 - John
                new Transaction(null, 1L, "John", 120.0, LocalDate.now().minusMonths(2)),
                new Transaction(null, 1L, "John", 75.0, LocalDate.now().minusMonths(1)),
                new Transaction(null, 1L, "John", 180.0, LocalDate.now().minusDays(20)),
                new Transaction(null, 1L, "John", 40.0, LocalDate.now().minusDays(10)),
                new Transaction(null, 1L, "John", 95.0, LocalDate.now()),

                // Customer 2 - David
                new Transaction(null, 2L, "David", 200.0, LocalDate.now().minusMonths(2)),
                new Transaction(null, 2L, "David", 95.0, LocalDate.now().minusMonths(1)),
                new Transaction(null, 2L, "David", 110.0, LocalDate.now().minusDays(25)),
                new Transaction(null, 2L, "David", 60.0, LocalDate.now().minusDays(12)),
                new Transaction(null, 2L, "David", 250.0, LocalDate.now()),

                // Customer 3 - Smith
                new Transaction(null, 3L, "Smith", 45.0, LocalDate.now().minusMonths(2)),
                new Transaction(null, 3L, "Smith", 70.0, LocalDate.now().minusMonths(1)),
                new Transaction(null, 3L, "Smith", 130.0, LocalDate.now().minusDays(18)),
                new Transaction(null, 3L, "Smith", 55.0, LocalDate.now().minusDays(7)),
                new Transaction(null, 3L, "Smith", 90.0, LocalDate.now()),

                // Customer 4 - Michael
                new Transaction(null, 4L, "Michael", 300.0, LocalDate.now().minusMonths(2)),
                new Transaction(null, 4L, "Michael", 80.0, LocalDate.now().minusMonths(1)),
                new Transaction(null, 4L, "Michael", 150.0, LocalDate.now().minusDays(15)),
                new Transaction(null, 4L, "Michael", 49.0, LocalDate.now().minusDays(5)),
                new Transaction(null, 4L, "Michael", 220.0, LocalDate.now()),

                // Customer 5 - Emma
                new Transaction(null, 5L, "Emma", 65.0, LocalDate.now().minusMonths(2)),
                new Transaction(null, 5L, "Emma", 140.0, LocalDate.now().minusMonths(1)),
                new Transaction(null, 5L, "Emma", 85.0, LocalDate.now().minusDays(20)),
                new Transaction(null, 5L, "Emma", 175.0, LocalDate.now().minusDays(8)),
                new Transaction(null, 5L, "Emma", 35.0, LocalDate.now())

        ));
    }
}