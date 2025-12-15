package com.empleos.web_empleos_backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WebEmpleosBackendApplicationTest {

    @Test
    void mainMethod_CanBeCalled() {
        assertDoesNotThrow(() -> {
            String[] args = new String[]{};
            // No ejecutamos realmente SpringApplication.run para evitar levantar el contexto
            WebEmpleosBackendApplication.main(args);
        });
    }

    @Test
    void class_HasSpringBootApplicationAnnotation() {
        assertTrue(WebEmpleosBackendApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void class_IsPublicAndHasMain() throws Exception {
        Class<?> clazz = WebEmpleosBackendApplication.class;
        assertTrue(java.lang.reflect.Modifier.isPublic(clazz.getModifiers()));
        assertNotNull(clazz.getMethod("main", String[].class));
    }
}