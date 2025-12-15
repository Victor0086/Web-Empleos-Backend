package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Notificacion;
import com.empleos.web_empleos_backend.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificacionServiceTest {
    @Mock
    private NotificacionRepository notificacionRepository;
    @InjectMocks
    private NotificacionService notificacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearNotificacion_guardaYRetornaNotificacion() {
        Notificacion notificacion = new Notificacion("destinatario1", "mensaje");
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);
        Notificacion result = notificacionService.crearNotificacion("destinatario1", "mensaje");
        assertEquals("destinatario1", result.getDestinatario());
        assertEquals("mensaje", result.getMensaje());
    }

    @Test
    void obtenerNotificaciones_filtraPorDestinatario() {
        Notificacion n1 = new Notificacion("dest1", "msg1");
        Notificacion n2 = new Notificacion("dest2", "msg2");
        Notificacion n3 = new Notificacion("dest1", "msg3");
        when(notificacionRepository.findAll()).thenReturn(List.of(n1, n2, n3));
        List<Notificacion> result = notificacionService.obtenerNotificaciones("dest1");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(n -> n.getDestinatario().equals("dest1")));
    }
}
