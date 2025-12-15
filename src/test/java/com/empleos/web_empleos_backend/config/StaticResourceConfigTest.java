package com.empleos.web_empleos_backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class StaticResourceConfigTest {

    @Test
    void addResourceHandlers_ConfiguresUploadPath() {
        StaticResourceConfig config = new StaticResourceConfig();
        ResourceHandlerRegistry registry = mock(ResourceHandlerRegistry.class);
        var registration = mock(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration.class);
        
        when(registry.addResourceHandler(any())).thenReturn(registration);
        when(registration.addResourceLocations(any(String.class))).thenReturn(registration);
        
        config.addResourceHandlers(registry);
        
        verify(registry).addResourceHandler("/uploads/**");
        verify(registration).addResourceLocations("file:uploads/");
    }
}