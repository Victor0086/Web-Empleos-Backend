package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.service.PostulacionService;
import com.empleos.web_empleos_backend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostulacionControllerTest {
    @Mock
    private PostulacionService postulacionService;
    @Mock
    private Jwt jwt;
    @Mock
    private UsuarioService usuarioService;
    @InjectMocks
    private PostulacionController postulacionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByEmail_retornaPostulaciones() {
        when(jwt.getClaim("preferred_username")).thenReturn("user@email.com");
        List<Postulacion> postulaciones = List.of(new Postulacion());
        when(postulacionService.findByEmail("user@email.com")).thenReturn(postulaciones);
        ResponseEntity<?> response = postulacionController.getByEmail(jwt);
        assertEquals(postulaciones, response.getBody());
    }

    @Test
    void getById_postulacionExiste_retornaPostulacion() {
        Postulacion postulacion = new Postulacion();
        when(postulacionService.findById(1L, "trab1")).thenReturn(Optional.of(postulacion));
        ResponseEntity<Postulacion> response = postulacionController.getById(1L, "trab1");
        assertEquals(postulacion, response.getBody());
    }

    @Test
    void getById_postulacionNoExiste_retornaNotFound() {
        when(postulacionService.findById(1L, "trab1")).thenReturn(Optional.empty());
        ResponseEntity<Postulacion> response = postulacionController.getById(1L, "trab1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void create_retornaPostulacionGuardada() {
        Postulacion postulacion = new Postulacion();
        when(postulacionService.save(postulacion)).thenReturn(postulacion);
        Postulacion result = postulacionController.create(postulacion);
        assertEquals(postulacion, result);
    }

    @Test
    void delete_eliminaPostulacion() {
        doNothing().when(postulacionService).deleteById(1L, "trab1");
        ResponseEntity<Void> response = postulacionController.delete(1L, "trab1");
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void getByOfertaId_retornaPostulaciones() {
        List<Postulacion> postulaciones = List.of(new Postulacion());
        when(postulacionService.findByOfertaId(1L)).thenReturn(postulaciones);
        List<Postulacion> result = postulacionController.getByOfertaId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void actualizarEstado_exito() {
        Map<String, String> body = Map.of("estado", "ACEPTADA");
        Postulacion postulacion = new Postulacion();
        when(postulacionService.findById(1L, "trab1")).thenReturn(Optional.of(postulacion));
        when(postulacionService.save(postulacion)).thenReturn(postulacion);
        ResponseEntity<?> response = postulacionController.actualizarEstado(1L, "trab1", body);
        Map<String, Object> respBody = (Map<String, Object>) response.getBody();
        assertEquals("Estado actualizado", respBody.get("message"));
        assertEquals("ACEPTADA", respBody.get("estado"));
    }

    @Test
    void actualizarEstado_faltaEstado() {
        Map<String, String> body = new HashMap<>();
        ResponseEntity<?> response = postulacionController.actualizarEstado(1L, "trab1", body);
        Map<String, Object> respBody = (Map<String, Object>) response.getBody();
        assertEquals("El estado es requerido", respBody.get("message"));
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void actualizarEstado_postulacionNoExiste() {
        Map<String, String> body = Map.of("estado", "ACEPTADA");
        when(postulacionService.findById(1L, "trab1")).thenReturn(Optional.empty());
        ResponseEntity<?> response = postulacionController.actualizarEstado(1L, "trab1", body);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void crearPostulacion_conArchivo_guardaArchivoYRetornaOk() throws Exception {
        // Mock usuarioService
        var usuario = new com.empleos.web_empleos_backend.model.Usuario();
        usuario.setUserId("trab1");
        when(usuarioService.buscarPorEmail("test@email.com"))
            .thenReturn(java.util.Optional.of(usuario));

        // Inyectar el mock manualmente usando reflexión
        java.lang.reflect.Field usuarioServiceField = PostulacionController.class.getDeclaredField("usuarioService");
        usuarioServiceField.setAccessible(true);
        usuarioServiceField.set(postulacionController, usuarioService);

        // Mock MultipartFile
        MockMultipartFile mockFile = new MockMultipartFile(
            "curriculum", "cv.pdf", MediaType.APPLICATION_PDF_VALUE, "contenido".getBytes()
        );

        // Mock postulacionService.save
        when(postulacionService.save(any(Postulacion.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = postulacionController.crearPostulacion(
            "Juan", "Pérez", "12345678-9", "987654321", "test@email.com",
            "2 años", "desc", "mot", 1L, mockFile
        );

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>)response.getBody()).get("message").toString().contains("Postulación enviada"));
    }

    @Test
    void getByEmail_claimEmails_retornaPostulaciones() {
        // preferred_username es null, emails es una lista con un email
        when(jwt.getClaim("preferred_username")).thenReturn(null);
        List<String> emails = List.of("otro@email.com");
        when(jwt.getClaim("emails")).thenReturn(emails);
        List<Postulacion> postulaciones = List.of(new Postulacion());
        when(postulacionService.findByEmail("otro@email.com")).thenReturn(postulaciones);
        ResponseEntity<?> response = postulacionController.getByEmail(jwt);
        assertEquals(postulaciones, response.getBody());
    }

        @Test
    void crearPostulacion_usuarioNoExiste_retornaBadRequest() throws Exception {
        // Simular usuarioService retorna Optional.empty()
        when(usuarioService.buscarPorEmail("noexiste@email.com")).thenReturn(java.util.Optional.empty());
        // Inyectar el mock manualmente usando reflexión
        java.lang.reflect.Field usuarioServiceField = PostulacionController.class.getDeclaredField("usuarioService");
        usuarioServiceField.setAccessible(true);
        usuarioServiceField.set(postulacionController, usuarioService);

        MockMultipartFile mockFile = new MockMultipartFile(
            "curriculum", "cv.pdf", MediaType.APPLICATION_PDF_VALUE, "contenido".getBytes()
        );

        ResponseEntity<?> response = postulacionController.crearPostulacion(
            "Juan", "Pérez", "12345678-9", "987654321", "noexiste@email.com",
            "2 años", "desc", "mot", 1L, mockFile
        );

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>)response.getBody()).get("message").toString().contains("No existe un usuario registrado"));
    }

    @Test
    void crearPostulacion_lanzaExcepcion_retornaError500() throws Exception {
        // Simular usuarioService lanza excepción
        when(usuarioService.buscarPorEmail(anyString())).thenThrow(new RuntimeException("fallo grave"));
        // Inyectar el mock manualmente usando reflexión
        java.lang.reflect.Field usuarioServiceField = PostulacionController.class.getDeclaredField("usuarioService");
        usuarioServiceField.setAccessible(true);
        usuarioServiceField.set(postulacionController, usuarioService);

        MockMultipartFile mockFile = new MockMultipartFile(
            "curriculum", "cv.pdf", MediaType.APPLICATION_PDF_VALUE, "contenido".getBytes()
        );

        ResponseEntity<?> response = postulacionController.crearPostulacion(
            "Juan", "Pérez", "12345678-9", "987654321", "error@email.com",
            "2 años", "desc", "mot", 1L, mockFile
        );

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>)response.getBody()).get("message").toString().contains("Error al procesar postulación"));
        assertTrue(((Map<?, ?>)response.getBody()).get("error").toString().contains("fallo grave"));
    }
}
