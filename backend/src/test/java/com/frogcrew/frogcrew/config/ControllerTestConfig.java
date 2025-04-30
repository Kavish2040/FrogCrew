package com.frogcrew.frogcrew.config;

import com.frogcrew.frogcrew.security.JwtTokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Base configuration for all controller tests.
 * All controller test classes should import this configuration.
 */
@TestConfiguration
@Import(TestSecurityConfig.class)
public class ControllerTestConfig {

    // Add any additional beans needed for all controller tests here

}