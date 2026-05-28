package com.sanjeev.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRewardResponse {

    private Long customerId;
    private String customerName;
    private List<MonthlyReward> monthlyRewards;
    private Long totalRewards;
}
