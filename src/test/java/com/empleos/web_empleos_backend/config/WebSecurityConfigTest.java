package com.empleos.web_empleos_backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

public class WebSecurityConfigTest {

    @Test
    void passwordEncoder_CreatesBCryptPasswordEncoder() {
        WebSecurityConfig config = new WebSecurityConfig();
        BCryptPasswordEncoder encoder = config.passwordEncoder();
        
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
        
        // Test encoding functionality
        String password = "testPassword123";
        String encoded = encoder.encode(password);
        assertNotNull(encoded);
        assertTrue(encoder.matches(password, encoded));
        assertFalse(encoder.matches("wrongPassword", encoded));
    }

    @Test
    void webSecurityConfig_CanBeInstantiated() {
        WebSecurityConfig config = new WebSecurityConfig();
        assertNotNull(config);
    }
}