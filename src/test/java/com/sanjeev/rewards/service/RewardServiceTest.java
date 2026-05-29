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

    @Test
    void shouldReturnCorrectRewardsForCustomer() {

        List<Transaction> transactions = List.of(
                new Transaction(
                        1L,
                        1L,
                        "John",
                        120.0,
                        LocalDate.of(2026, 3, 15)
                ),
                new Transaction(
                        2L,
                        1L,
                        "John",
                        75.0,
                        LocalDate.of(2026, 4, 10)
                )
        );

        when(transactionRepository.findByCustomerId(1L))
                .thenReturn(transactions);

        CustomerRewardResponse response =
                rewardService.getRewardsByCustomerId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
        assertEquals("John", response.getCustomerName());
        assertEquals(115L, response.getTotalRewards());
    }

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

        when(transactionRepository.findByCustomerId(1L))
                .thenReturn(transactions);

        CustomerRewardResponse response =
                rewardService.getRewardsByCustomerId(1L);

        assertEquals(0L, response.getTotalRewards());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {

        when(transactionRepository.findByCustomerId(999L))
                .thenReturn(List.of());

        assertThrows(
                CustomerNotFoundException.class,
                () -> rewardService.getRewardsByCustomerId(999L)
        );
    }

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

        when(transactionRepository.findByCustomerId(1L))
                .thenReturn(transactions);

        CustomerRewardResponse response =
                rewardService.getRewardsByCustomerId(1L);

        assertEquals(50L, response.getTotalRewards());
    }

}
