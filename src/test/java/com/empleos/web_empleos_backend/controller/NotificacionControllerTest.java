package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Notificacion;
import com.empleos.web_empleos_backend.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificacionControllerTest {
    @Mock
    private NotificacionService notificacionService;
    @InjectMocks
    private NotificacionController notificacionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crear_notificacionValida_retornaNotificacion() {
        Map<String, String> body = Map.of("destinatario", "user@test.com", "mensaje", "Hola");
        Notificacion notificacion = new Notificacion("user@test.com", "Hola");
        when(notificacionService.crearNotificacion("user@test.com", "Hola")).thenReturn(notificacion);
        ResponseEntity<Notificacion> response = notificacionController.crear(body);
        assertEquals(notificacion, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void crear_faltanCampos_retornaBadRequest() {
        Map<String, String> body = Map.of("destinatario", "user@test.com");
        ResponseEntity<Notificacion> response = notificacionController.crear(body);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void obtener_retornaListaNotificaciones() {
        List<Notificacion> notificaciones = List.of(new Notificacion("user@test.com", "Hola"));
        when(notificacionService.obtenerNotificaciones("user@test.com")).thenReturn(notificaciones);
        ResponseEntity<List<Notificacion>> response = notificacionController.obtener("user@test.com");
        assertEquals(notificaciones, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
