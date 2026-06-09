package com.sanjeev.rewards.controller;

import com.sanjeev.rewards.dto.CustomerRewardResponse;
import com.sanjeev.rewards.dto.MonthlyReward;
import com.sanjeev.rewards.exception.CustomerNotFoundException;
import com.sanjeev.rewards.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;

    /**
     * Positive Test Case
     * Verify all customer rewards are returned successfully.
     */
    @Test
    void shouldReturnAllCustomerRewards() throws Exception {

        List<MonthlyReward> monthlyRewards = List.of(
                new MonthlyReward("2026-03", 90L),
                new MonthlyReward("2026-04", 25L)
        );

        List<CustomerRewardResponse> responses = List.of(
                new CustomerRewardResponse(
                        1L,
                        "John",
                        monthlyRewards,
                        115L
                )
        );

        when(rewardService.getAllCustomerRewards())
                .thenReturn(responses);

        mockMvc.perform(
                        get("/api/rewards")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].customerName").value("John"))
                .andExpect(jsonPath("$[0].totalRewards").value(115L));
    }

    /**
     * Positive Test Case
     * Verify reward details for a specific customer.
     */
    @Test
    void shouldReturnRewardsForSpecificCustomer() throws Exception {

        List<MonthlyReward> monthlyRewards = List.of(
                new MonthlyReward("2026-03", 90L)
        );

        CustomerRewardResponse response =
                new CustomerRewardResponse(
                        1L,
                        "John",
                        monthlyRewards,
                        90L
                );

        when(rewardService.getRewardsByCustomerId(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/rewards/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.customerName").value("John"))
                .andExpect(jsonPath("$.totalRewards").value(90L));
    }

    /**
     * Negative Test Case
     * Verify 404 is returned when customer does not exist.
     */
    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {

        when(rewardService.getRewardsByCustomerId(999L))
                .thenThrow(
                        new CustomerNotFoundException(
                                "Customer not found with ID: 999"
                        )
                );

        mockMvc.perform(
                        get("/api/rewards/999")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Customer not found with ID: 999"));
    }

    /**
     * Negative Test Case
     * Verify generic exceptions are handled properly.
     */
    @Test
    void shouldReturnInternalServerError() throws Exception {

        when(rewardService.getAllCustomerRewards())
                .thenThrow(
                        new RuntimeException("Database error")
                );

        mockMvc.perform(
                        get("/api/rewards")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error")
                        .value("Internal Server Error"))
                .andExpect(jsonPath("$.message")
                        .value("Database error"));
    }
}