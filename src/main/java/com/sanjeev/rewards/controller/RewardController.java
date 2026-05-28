package com.sanjeev.rewards.controller;

import com.sanjeev.rewards.dto.CustomerRewardResponse;
import com.sanjeev.rewards.service.RewardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    // Get reward details for all customers.
    @GetMapping
    public List<CustomerRewardResponse> getAllCustomerRewards() {
        return rewardService.getAllCustomerRewards();
    }

    // Get reward details for a specific customer.
    @GetMapping("/{customerId}")
    public CustomerRewardResponse getRewardsByCustomerId(
            @PathVariable Long customerId) {

        return rewardService.getRewardsByCustomerId(customerId);
    }

}
