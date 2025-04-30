package com.frogcrew.frogcrew.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Base class for controller tests with common configurations and utilities
 */
public abstract class BaseControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    /**
     * Initialize MockMvc with security configurations
     */
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    /**
     * Helper method to generate random UUIDs
     * 
     * @return a random UUID
     */
    protected UUID randomUUID() {
        return UUID.randomUUID();
    }

    /**
     * Helper method to convert an object to JSON string
     * 
     * @param obj the object to convert
     * @return the JSON string representation
     * @throws Exception if serialization fails
     */
    protected String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}