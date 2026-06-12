package com.sanjeev.rewards.service;

import com.sanjeev.rewards.dto.CustomerRewardResponse;
import com.sanjeev.rewards.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RewardServiceIntegrationTest {

    @Autowired
    private RewardService rewardService;

    @Test
    void shouldReturnAllCustomerRewards() {

        List<CustomerRewardResponse> responses =
                rewardService.getAllCustomerRewards();

        assertFalse(responses.isEmpty());
    }

    @Test
    void shouldReturnCustomerRewards() {

        CustomerRewardResponse response =
                rewardService.getRewardsByCustomerId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
    }

    @Test
    void shouldThrowExceptionForInvalidCustomer() {

        assertThrows(
                CustomerNotFoundException.class,
                () -> rewardService.getRewardsByCustomerId(999L)
        );
    }
}
