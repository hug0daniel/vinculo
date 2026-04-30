package com.vinculo.integration.api.disaster.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Disaster Controller Integration Tests")
class DisasterControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("should list disasters via API")
    void shouldListDisasters() throws Exception {
        mockMvc.perform(get("/api/v1/disasters"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should create disaster via API")
    void shouldCreateDisaster() throws Exception {
        String requestJson = """
            {
                "name": "Enchente SP 2026",
                "type": "FLOOD",
                "location": "São Paulo"
            }
            """;

        mockMvc.perform(post("/api/v1/disasters")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Enchente SP 2026"))
                .andExpect(jsonPath("$.type").value("FLOOD"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
