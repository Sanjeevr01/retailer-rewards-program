package com.sanjeev.rewards.service;

import com.sanjeev.rewards.dto.CustomerRewardResponse;
import com.sanjeev.rewards.entity.Transaction;
import com.sanjeev.rewards.exception.CustomerNotFoundException;
import com.sanjeev.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Positive Test
     * Amount = 120
     * Reward = 90
     */
    @Test
    void shouldReturnCorrectRewardsForCustomer() {

        List<Transaction> transactions = List.of(
                new Transaction(
                        1L,
                        1L,
                        "John",
                        120.0,
                        LocalDate.now().minusDays(10)
                )
        );

        when(transactionRepository
                .findByCustomerIdAndTransactionDateAfter(
                        any(Long.class),
                        any(LocalDate.class)))
                .thenReturn(transactions);

        CustomerRewardResponse response =
                rewardService.getRewardsByCustomerId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
        assertEquals("John", response.getCustomerName());
        assertEquals(90L, response.getTotalRewards());
    }

    /**
     * Positive Test
     * Amount <= 50
     * Reward = 0
     */
    @Test
    void shouldReturnZeroRewardsForAmountLessThan50() {

        List<Transaction> transactions = List.of(
                new Transaction(
                        1L,
                        1L,
                        "John",
                        40.0,
                        LocalDate.now()
                )
        );

        when(transactionRepository
                .findByCustomerIdAndTransactionDateAfter(
                        any(Long.class),
                        any(LocalDate.class)))
                .thenReturn(transactions);

        CustomerRewardResponse response =
                rewardService.getRewardsByCustomerId(1L);

        assertEquals(0L, response.getTotalRewards());
    }

    /**
     * Positive Test
     * Amount = 100
     * Reward = 50
     */
    @Test
    void shouldReturnFiftyPointsForHundredDollars() {

        List<Transaction> transactions = List.of(
                new Transaction(
                        1L,
                        1L,
                        "John",
                        100.0,
                        LocalDate.now()
                )
        );

        when(transactionRepository
                .findByCustomerIdAndTransactionDateAfter(
                        any(Long.class),
                        any(LocalDate.class)))
                .thenReturn(transactions);

        CustomerRewardResponse response =
                rewardService.getRewardsByCustomerId(1L);

        assertEquals(50L, response.getTotalRewards());
    }

    /**
     * Positive Test
     * Verify getAllCustomerRewards()
     */
    @Test
    void shouldReturnAllCustomerRewards() {

        List<Transaction> transactions = List.of(
                new Transaction(
                        1L,
                        1L,
                        "John",
                        120.0,
                        LocalDate.now()
                ),
                new Transaction(
                        2L,
                        2L,
                        "David",
                        200.0,
                        LocalDate.now()
                )
        );

        when(transactionRepository
                .findByTransactionDateAfter(any(LocalDate.class)))
                .thenReturn(transactions);

        List<CustomerRewardResponse> responses =
                rewardService.getAllCustomerRewards();

        assertEquals(2, responses.size());
    }

    /**
     * Positive Test
     * Verify multiple transactions are summed correctly.
     * 120 = 90 points
     * 75 = 25 points
     * Total = 115 points
     */
    @Test
    void shouldCalculateTotalRewardsFromMultipleTransactions() {

        List<Transaction> transactions = List.of(
                new Transaction(
                        1L,
                        1L,
                        "John",
                        120.0,
                        LocalDate.now()
                ),
                new Transaction(
                        2L,
                        1L,
                        "John",
                        75.0,
                        LocalDate.now()
                )
        );

        when(transactionRepository
                .findByCustomerIdAndTransactionDateAfter(
                        any(Long.class),
                        any(LocalDate.class)))
                .thenReturn(transactions);

        CustomerRewardResponse response =
                rewardService.getRewardsByCustomerId(1L);

        assertEquals(115L, response.getTotalRewards());
    }

    /**
     * Negative Test
     * Customer not found.
     */
    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {

        when(transactionRepository
                .findByCustomerIdAndTransactionDateAfter(
                        any(Long.class),
                        any(LocalDate.class)))
                .thenReturn(List.of());

        assertThrows(
                CustomerNotFoundException.class,
                () -> rewardService.getRewardsByCustomerId(999L)
        );
    }
}