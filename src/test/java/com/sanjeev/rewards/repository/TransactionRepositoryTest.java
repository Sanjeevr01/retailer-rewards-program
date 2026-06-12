package com.sanjeev.rewards.repository;

import com.sanjeev.rewards.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Positive Test Case
     * Verify repository returns transactions
     * for a given customer after a specific date.
     */
    @Test
    void shouldFindTransactionsByCustomerIdAndDate() {

        // Arrange
        Transaction transaction1 = new Transaction(
                null,
                1L,
                "John",
                120.0,
                LocalDate.now().minusDays(20)
        );

        Transaction transaction2 = new Transaction(
                null,
                1L,
                "John",
                75.0,
                LocalDate.now().minusDays(10)
        );

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        // Act
        List<Transaction> result =
                transactionRepository
                        .findByCustomerIdAndTransactionDateAfter(
                                1L,
                                LocalDate.now().minusMonths(3)
                        );

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getCustomerName());
    }

    /**
     * Positive Test Case
     * Verify repository returns all transactions
     * after a given date.
     */
    @Test
    void shouldFindTransactionsAfterDate() {

        // Arrange
        Transaction transaction1 = new Transaction(
                null,
                1L,
                "John",
                120.0,
                LocalDate.now().minusDays(15)
        );

        Transaction transaction2 = new Transaction(
                null,
                2L,
                "David",
                200.0,
                LocalDate.now().minusDays(5)
        );

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        // Act
        List<Transaction> result =
                transactionRepository.findByTransactionDateAfter(
                        LocalDate.now().minusMonths(3)
                );

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * Negative Test Case
     * Verify empty list is returned when
     * customer does not exist.
     */
    @Test
    void shouldReturnEmptyListWhenCustomerNotFound() {

        // Act
        List<Transaction> result =
                transactionRepository
                        .findByCustomerIdAndTransactionDateAfter(
                                999L,
                                LocalDate.now().minusMonths(3)
                        );

        // Assert
        assertTrue(result.isEmpty());
    }

    /**
     * Negative Test Case
     * Verify empty list is returned when
     * transactions are older than the filter date.
     */
    @Test
    void shouldReturnEmptyListForOldTransactions() {

        // Arrange
        Transaction transaction = new Transaction(
                null,
                1L,
                "John",
                100.0,
                LocalDate.now().minusMonths(6)
        );

        transactionRepository.save(transaction);

        // Act
        List<Transaction> result =
                transactionRepository.findByTransactionDateAfter(
                        LocalDate.now().minusMonths(3)
                );

        // Assert
        assertTrue(result.isEmpty());
    }
}