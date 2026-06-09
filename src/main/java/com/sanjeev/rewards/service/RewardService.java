package com.sanjeev.rewards.service;

import com.sanjeev.rewards.dto.CustomerRewardResponse;
import com.sanjeev.rewards.dto.MonthlyReward;
import com.sanjeev.rewards.entity.Transaction;
import com.sanjeev.rewards.exception.CustomerNotFoundException;
import com.sanjeev.rewards.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private static final int FIFTY_DOLLARS = 50;
    private static final int HUNDRED_DOLLARS = 100;

    private final TransactionRepository transactionRepository;

    public RewardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Returns rewards for all customers for the last 3 months.
    public List<CustomerRewardResponse> getAllCustomerRewards() {

        // Calculating date exactly 3 months before
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        // Fetching all transactions after the calculated date
        List<Transaction> transactions =
                transactionRepository.findByTransactionDateAfter(
                        threeMonthsAgo
                );

        // Grouping transactions by customer ID
        Map<Long, List<Transaction>> customerTransactions =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                Transaction::getCustomerId
                        ));

        // Converting each customer transaction list into a CustomerRewardResponse Object and return the list of responses
        return customerTransactions.entrySet()
                .stream()
                .map(entry ->
                        buildCustomerRewardResponse(
                                entry.getKey(),
                                entry.getValue()
                        )
                )
                .toList();
    }

    // Returns rewards for a specific customer for the last 3 months.
    public CustomerRewardResponse getRewardsByCustomerId(
            Long customerId) {

        // Calculating date exactly 3 months before
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        // Fetching transactions for the given customer only for last 3 months
        List<Transaction> transactions =
                transactionRepository
                        .findByCustomerIdAndTransactionDateAfter(
                                customerId,
                                threeMonthsAgo
                        );

        // If no transactions found, throw custom exception
        if (transactions.isEmpty()) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: " + customerId
            );
        }

        // Build final response object
        return buildCustomerRewardResponse(
                customerId,
                transactions
        );
    }

    // Builds final customer response.
    private CustomerRewardResponse buildCustomerRewardResponse(
            Long customerId,
            List<Transaction> transactions) {

        // Assuming all transactions belong to the same customer, we can get the customer name from the first transaction
        String customerName =
                transactions.get(0).getCustomerName();

        // Calculating monthly rewards
        Map<YearMonth, Long> monthlyRewardsMap =
                calculateMonthlyRewards(transactions);

        // Converting monthly rewards map to a list of MonthlyReward objects
        List<MonthlyReward> monthlyRewards =
                monthlyRewardsMap.entrySet()
                        .stream()
                        .map(entry ->
                                new MonthlyReward(
                                        entry.getKey().toString(),
                                        entry.getValue()
                                )
                        )
                        .toList();

        // Calculating total rewards
        Long totalRewards =
                calculateTotalRewards(monthlyRewardsMap);

        // Building and returning the final response object
        return new CustomerRewardResponse(
                customerId,
                customerName,
                monthlyRewards,
                totalRewards
        );
    }

    // Calculates rewards grouped by month.
    private Map<YearMonth, Long> calculateMonthlyRewards(
            List<Transaction> transactions) {

        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction ->
                                YearMonth.from(
                                        transaction.getTransactionDate()
                                ),
                        Collectors.summingLong(
                                transaction ->
                                        calculateRewardPoints(
                                                transaction.getAmount()
                                        )
                        )
                ));
    }

    // Calculates total reward points.
    private Long calculateTotalRewards(
            Map<YearMonth, Long> monthlyRewardsMap) {

        return monthlyRewardsMap.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    // Reward calculation logic.
    private Long calculateRewardPoints(Double amount) {

        // No rewards for transactions of $50 or less
        if (amount <= FIFTY_DOLLARS) {
            return 0L;
        }

        // 1 reward point for every dollar spent over $50 up to $100
        if (amount <= HUNDRED_DOLLARS) {
            return (long) (amount - FIFTY_DOLLARS);
        }

        // 2 reward points for every dollar spent over $100
        return (long) (
                (amount - HUNDRED_DOLLARS) * 2
                        + FIFTY_DOLLARS
        );
    }
}