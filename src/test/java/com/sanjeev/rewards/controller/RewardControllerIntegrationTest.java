package com.sanjeev.rewards.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllCustomerRewards() throws Exception {

        mockMvc.perform(
                        get("/api/rewards")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    void shouldReturnCustomerRewards() throws Exception {

        mockMvc.perform(
                        get("/api/rewards/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    void shouldReturnNotFoundForInvalidCustomer() throws Exception {

        mockMvc.perform(
                        get("/api/rewards/999")
                )
                .andExpect(status().isNotFound());
    }
}
