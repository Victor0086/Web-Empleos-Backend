package com.empleos.web_empleos_backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class WebConfigTest {

    @Test
    void corsConfigurer_CreatesWebMvcConfigurer() {
        WebConfig webConfig = new WebConfig();
        WebMvcConfigurer configurer = webConfig.corsConfigurer();
        assertNotNull(configurer);
    }

    @Test
    void corsConfigurer_ConfiguresCorsCorrectly() {
        WebConfig webConfig = new WebConfig();
        WebMvcConfigurer configurer = webConfig.corsConfigurer();
        
        CorsRegistry registry = mock(CorsRegistry.class);
        var registration = mock(org.springframework.web.servlet.config.annotation.CorsRegistration.class);
        
        when(registry.addMapping(anyString())).thenReturn(registration);
        when(registration.allowedOrigins(anyString())).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);
        
        configurer.addCorsMappings(registry);
        
        verify(registry).addMapping("/**");
        verify(registration).allowedOrigins("http://localhost:4200");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(registration).allowedHeaders("Authorization", "Content-Type");
        verify(registration).allowCredentials(true);
    }
}