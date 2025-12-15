package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Contrato;
import com.empleos.web_empleos_backend.service.ContratoService;
import com.empleos.web_empleos_backend.service.PostulacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContratoControllerTest {
    @Mock
    private ContratoService contratoService;
    @Mock
    private PostulacionService postulacionService;
    @Mock
    private Jwt jwt;
    @InjectMocks
    private ContratoController contratoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_retornaListaContratos() {
        List<Contrato> contratos = List.of(new Contrato(), new Contrato());
        when(contratoService.findAll()).thenReturn(contratos);
        List<Contrato> result = contratoController.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void getMisContratos_retornaContratosUsuario() {
        when(jwt.getSubject()).thenReturn("user1");
        when(jwt.getClaim("preferred_username")).thenReturn("user@email.com");
        List<Contrato> contratos = List.of(new Contrato());
        when(contratoService.findContratosPorTrabajadorOEmpleadorOEmail("user1", "user@email.com")).thenReturn(contratos);
        ResponseEntity<List<Contrato>> response = contratoController.getMisContratos(jwt);
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getById_contratoExiste_retornaContrato() {
        Contrato contrato = new Contrato();
        when(contratoService.findById(1L)).thenReturn(Optional.of(contrato));
        ResponseEntity<Contrato> response = contratoController.getById(1L);
        assertEquals(contrato, response.getBody());
    }

    @Test
    void getById_contratoNoExiste_retornaNotFound() {
        when(contratoService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Contrato> response = contratoController.getById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void rechazarContrato_exito() {
        when(jwt.getSubject()).thenReturn("user1");
        when(jwt.getClaim("preferred_username")).thenReturn("user@email.com");
        doNothing().when(contratoService).rechazarContrato(1L, "user1", "user@email.com");
        ResponseEntity<?> response = contratoController.rechazarContrato(1L, jwt);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Contrato rechazado correctamente", body.get("message"));
    }

    @Test
    void rechazarContrato_error() {
        when(jwt.getSubject()).thenReturn("user1");
        when(jwt.getClaim("preferred_username")).thenReturn("user@email.com");
        doThrow(new RuntimeException("No tienes permisos")).when(contratoService).rechazarContrato(1L, "user1", "user@email.com");
        ResponseEntity<?> response = contratoController.rechazarContrato(1L, jwt);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("No tienes permisos", body.get("error"));
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void delete_eliminaContrato() {
        doNothing().when(contratoService).deleteById(1L);
        ResponseEntity<Void> response = contratoController.delete(1L);
        assertEquals(204, response.getStatusCodeValue());
    }


        @Test
    void create_faltanIds_retornaBadRequest() {
        Map<String, Object> body = new HashMap<>();
        ResponseEntity<?> response = contratoController.create(body);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>)response.getBody()).get("message").toString().contains("Faltan ofertaId o trabajadorId"));
    }

    @Test
    void create_postulacionNoEncontrada_retornaBadRequest() {
        Map<String, Object> body = new HashMap<>();
        body.put("ofertaId", 1L);
        body.put("trabajadorId", "trab1");
        when(postulacionService.findById(1L, "trab1")).thenReturn(Optional.empty());
        ResponseEntity<?> response = contratoController.create(body);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>)response.getBody()).get("message").toString().contains("Postulación no encontrada"));
    }

    @Test
    void create_lanzaExcepcion_retornaError500() {
        Map<String, Object> body = new HashMap<>();
        body.put("ofertaId", 1L);
        body.put("trabajadorId", "trab1");
        when(postulacionService.findById(1L, "trab1")).thenThrow(new RuntimeException("fallo grave"));
        ResponseEntity<?> response = contratoController.create(body);
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>)response.getBody()).get("message").toString().contains("Error al crear contrato"));
        assertTrue(((Map<?, ?>)response.getBody()).get("error").toString().contains("fallo grave"));
    }

        @Test
    void firmarContrato_comoTrabajador_retornaOk() {
        Long id = 1L;
        String idUsuario = "trabajador1";
        String email = "trabajador@email.com";
        Contrato contrato = new Contrato();
        com.empleos.web_empleos_backend.model.Postulacion postulacion = new com.empleos.web_empleos_backend.model.Postulacion();
        postulacion.setTrabajadorId(idUsuario);
        contrato.setPostulacion(postulacion);
        when(jwt.getSubject()).thenReturn(idUsuario);
        when(jwt.getClaim("preferred_username")).thenReturn(email);
        when(contratoService.firmarContrato(id, idUsuario, email)).thenReturn(contrato);
        ResponseEntity<?> response = contratoController.firmarContrato(id, jwt);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(body.get("mensaje").toString().contains("Trabajador"));
        assertEquals(contrato, body.get("contrato"));
    }

    @Test
    void firmarContrato_comoEmpleadorONotario_retornaOk() {
        Long id = 1L;
        String idUsuario = "empleador1";
        String email = "empleador@email.com";
        Contrato contrato = new Contrato();
        com.empleos.web_empleos_backend.model.Postulacion postulacion = new com.empleos.web_empleos_backend.model.Postulacion();
        postulacion.setTrabajadorId("trabajador1");
        contrato.setPostulacion(postulacion);
        when(jwt.getSubject()).thenReturn(idUsuario);
        when(jwt.getClaim("preferred_username")).thenReturn(email);
        when(contratoService.firmarContrato(id, idUsuario, email)).thenReturn(contrato);
        ResponseEntity<?> response = contratoController.firmarContrato(id, jwt);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(body.get("mensaje").toString().contains("Empleador / Notario"));
        assertEquals(contrato, body.get("contrato"));
    }

    @Test
    void firmarContrato_lanzaExcepcion_retornaBadRequest() {
        Long id = 1L;
        String idUsuario = "trabajador1";
        String email = "trabajador@email.com";
        when(jwt.getSubject()).thenReturn(idUsuario);
        when(jwt.getClaim("preferred_username")).thenReturn(email);
        when(contratoService.firmarContrato(id, idUsuario, email)).thenThrow(new RuntimeException("No se puede firmar"));
        ResponseEntity<?> response = contratoController.firmarContrato(id, jwt);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>)response.getBody()).get("error").toString().contains("No se puede firmar"));
    }


        @Test
    void create_exito_conIdNotarioYEstado() {
        Map<String, Object> body = new HashMap<>();
        body.put("ofertaId", 1L);
        body.put("trabajadorId", "trab1");
        body.put("idNotario", "notario123");
        body.put("estado", "FIRMADO");

        com.empleos.web_empleos_backend.model.Postulacion postulacion = new com.empleos.web_empleos_backend.model.Postulacion();
        postulacion.setEstado("ENVIADA");
        Optional<com.empleos.web_empleos_backend.model.Postulacion> postulacionOpt = Optional.of(postulacion);
        when(postulacionService.findById(1L, "trab1")).thenReturn(postulacionOpt);
        when(postulacionService.save(any())).thenReturn(postulacion);

        Contrato contratoGuardado = new Contrato();
        when(contratoService.save(any(Contrato.class))).thenAnswer(invocation -> {
            Contrato c = invocation.getArgument(0);
            // Simular que el contrato guardado es el mismo que se pasa
            return c;
        });

        ResponseEntity<?> response = contratoController.create(body);
        assertEquals(200, response.getStatusCodeValue());
        Contrato contratoResp = (Contrato) response.getBody();
        assertNotNull(contratoResp);
        assertEquals(postulacion, contratoResp.getPostulacion());
        assertEquals("notario123", contratoResp.getIdNotario());
        assertEquals("FIRMADO", contratoResp.getEstado());
        // Verifica que el estado de la postulación fue cambiado
        assertEquals("CONTRATO_GENERADO", postulacion.getEstado());
    }

    @Test
    void firmarContrato_emailDesdeClaimEmails() {
        Long id = 1L;
        String idUsuario = "trabajador1";
        // preferred_username es null, emails es una lista
        when(jwt.getSubject()).thenReturn(idUsuario);
        when(jwt.getClaim("preferred_username")).thenReturn(null);
        List<String> emails = List.of("otro@email.com");
        when(jwt.getClaim("emails")).thenReturn(emails);
        Contrato contrato = new Contrato();
        com.empleos.web_empleos_backend.model.Postulacion postulacion = new com.empleos.web_empleos_backend.model.Postulacion();
        postulacion.setTrabajadorId(idUsuario);
        contrato.setPostulacion(postulacion);
        when(contratoService.firmarContrato(id, idUsuario, "otro@email.com")).thenReturn(contrato);
        ResponseEntity<?> response = contratoController.firmarContrato(id, jwt);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(body.get("mensaje").toString().contains("Trabajador"));
        assertEquals(contrato, body.get("contrato"));
    }

    @Test
    void getMisContratos_emailDesdeClaimEmails() {
        String idUsuario = "user1";
        when(jwt.getSubject()).thenReturn(idUsuario);
        when(jwt.getClaim("preferred_username")).thenReturn(null);
        List<String> emails = List.of("otro@email.com");
        when(jwt.getClaim("emails")).thenReturn(emails);
        List<Contrato> contratos = List.of(new Contrato());
        when(contratoService.findContratosPorTrabajadorOEmpleadorOEmail(idUsuario, "otro@email.com")).thenReturn(contratos);
        ResponseEntity<List<Contrato>> response = contratoController.getMisContratos(jwt);
        assertEquals(1, response.getBody().size());
    }


    @Test
    void rechazarContrato_emailDesdeClaimEmails() {
        Long id = 1L;
        String idUsuario = "user1";
        when(jwt.getSubject()).thenReturn(idUsuario);
        when(jwt.getClaim("preferred_username")).thenReturn(null);
        List<String> emails = List.of("otro@email.com");
        when(jwt.getClaim("emails")).thenReturn(emails);
        doNothing().when(contratoService).rechazarContrato(id, idUsuario, "otro@email.com");
        ResponseEntity<?> response = contratoController.rechazarContrato(id, jwt);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(body.get("message").toString().contains("Contrato rechazado correctamente"));
    }
}
