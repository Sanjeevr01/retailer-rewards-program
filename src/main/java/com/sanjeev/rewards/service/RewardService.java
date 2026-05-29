package com.sanjeev.rewards.service;

import com.sanjeev.rewards.dto.CustomerRewardResponse;
import com.sanjeev.rewards.dto.MonthlyReward;
import com.sanjeev.rewards.entity.Transaction;
import com.sanjeev.rewards.exception.CustomerNotFoundException;
import com.sanjeev.rewards.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private final TransactionRepository transactionRepository;

    public RewardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Get rewards for all customers.
    public List<CustomerRewardResponse> getAllCustomerRewards() {

        List<Transaction> transactions = transactionRepository.findAll();

        Map<Long, List<Transaction>> customerTransactions = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCustomerId));

        List<CustomerRewardResponse> responseList = new ArrayList<>();

        for (Map.Entry<Long, List<Transaction>> entry : customerTransactions.entrySet()) {

            List<Transaction> customerTransactionList = entry.getValue();

            String customerName = customerTransactionList.get(0).getCustomerName();

            Map<YearMonth, Long> monthlyRewardsMap =
                    customerTransactionList.stream()
                            .collect(Collectors.groupingBy(
                                    transaction -> YearMonth.from(transaction.getTransactionDate()),
                                    Collectors.summingLong(transaction ->
                                            calculateRewardPoints(transaction.getAmount()))
                            ));

            List<MonthlyReward> monthlyRewards = monthlyRewardsMap.entrySet()
                    .stream()
                    .map(monthEntry -> new MonthlyReward(
                            monthEntry.getKey().toString(),
                            monthEntry.getValue()))
                    .toList();

            Long totalRewards = monthlyRewardsMap.values()
                    .stream()
                    .mapToLong(Long::longValue)
                    .sum();

            CustomerRewardResponse response = new CustomerRewardResponse(
                    entry.getKey(),
                    customerName,
                    monthlyRewards,
                    totalRewards
            );

            responseList.add(response);
        }

        return responseList;
    }

    // Get rewards for a specific customer.
    public CustomerRewardResponse getRewardsByCustomerId(Long customerId) {

        List<Transaction> transactions =
                transactionRepository.findByCustomerId(customerId);

        if (transactions.isEmpty()) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: " + customerId
            );
        }

        String customerName = transactions.get(0).getCustomerName();

        Map<YearMonth, Long> monthlyRewardsMap =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                transaction -> YearMonth.from(transaction.getTransactionDate()),
                                Collectors.summingLong(transaction ->
                                        calculateRewardPoints(transaction.getAmount()))
                        ));

        List<MonthlyReward> monthlyRewards = monthlyRewardsMap.entrySet()
                .stream()
                .map(entry -> new MonthlyReward(
                        entry.getKey().toString(),
                        entry.getValue()))
                .toList();

        Long totalRewards = monthlyRewardsMap.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();

        return new CustomerRewardResponse(
                customerId,
                customerName,
                monthlyRewards,
                totalRewards
        );
    }

    // Calculate reward points based on transaction amount.
    private Long calculateRewardPoints(Double amount) {

        if (amount <= 50) {
            return 0L;
        }

        if (amount <= 100) {
            return (long) (amount - 50);
        }

        return (long) ((amount - 100) * 2 + 50);
    }
}