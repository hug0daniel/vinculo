package com.vinculo.integration.api.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Request Controller Integration Tests")
class RequestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("should list requests via API")
    void shouldListRequests() throws Exception {
        mockMvc.perform(get("/api/v1/requests"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should create request via API")
    void shouldCreateRequest() throws Exception {
        String requestJson = """
            {
                "beneficiary": {
                    "name": "John Doe",
                    "contact": "john@test.com",
                    "documentId": "DOC-001"
                },
                "disasterId": "00000000-0000-0000-0000-000000000001",
                "items": [
                    {
                        "productName": "Rice",
                        "quantity": 50,
                        "unit": "KG"
                    }
                ]
            }
            """;

        mockMvc.perform(post("/api/v1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.beneficiary.name").value("John Doe"))
                .andExpect(jsonPath("$.items.length()").value(1));
    }
}
