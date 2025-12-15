package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.dto.EstudioDTO;
import com.empleos.web_empleos_backend.dto.PerfilDTO;
import com.empleos.web_empleos_backend.model.Estudio;
import com.empleos.web_empleos_backend.model.Usuario;
import com.empleos.web_empleos_backend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private UsuarioService usuarioService;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_usuarioNuevo_retornaSuccess() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        when(usuarioService.emailExiste(usuario.getEmail())).thenReturn(false);
        when(usuarioService.registrarUsuario(usuario)).thenReturn(usuario);
        ResponseEntity<?> response = authController.register(usuario);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) body.get("success"));
        assertEquals(usuario, body.get("usuario"));
    }

    @Test
    void register_emailExistente_retornaError() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        when(usuarioService.emailExiste(usuario.getEmail())).thenReturn(true);
        ResponseEntity<?> response = authController.register(usuario);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("El email ya está registrado", body.get("error"));
    }

    @Test
    void login_credencialesValidas_retornaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        Map<String, String> loginData = Map.of("email", "test@test.com", "password", "1234");
        when(usuarioService.login("test@test.com", "1234")).thenReturn(Optional.of(usuario));
        ResponseEntity<?> response = authController.login(loginData);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) body.get("success"));
        assertEquals(usuario, body.get("usuario"));
    }

    @Test
    void login_credencialesInvalidas_retornaError() {
        Map<String, String> loginData = Map.of("email", "test@test.com", "password", "wrong");
        when(usuarioService.login("test@test.com", "wrong")).thenReturn(Optional.empty());
        ResponseEntity<?> response = authController.login(loginData);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Credenciales incorrectas", body.get("error"));
    }

    @Test
    void getUserByEmail_usuarioExiste_retornaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        when(usuarioService.buscarPorEmail("test@test.com")).thenReturn(Optional.of(usuario));
        ResponseEntity<?> response = authController.getUserByEmail("test@test.com");
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(usuario, body.get("usuario"));
    }

    @Test
    void getUserByEmail_usuarioNoExiste_retornaError() {
        when(usuarioService.buscarPorEmail("no@existe.com")).thenReturn(Optional.empty());
        ResponseEntity<?> response = authController.getUserByEmail("no@existe.com");
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Usuario no encontrado", body.get("error"));
    }

    @Test
    void actualizarEducacion_usuarioExiste_retornaEducacion() {
        EstudioDTO dto = new EstudioDTO();
        dto.setUserId("1");
        Estudio estudio = new Estudio();
        when(usuarioService.agregarEstudio("1", dto)).thenReturn(estudio);
        ResponseEntity<?> response = authController.actualizarEducacion(null, dto);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(estudio, body.get("educacion"));
    }

    @Test
    void actualizarEducacion_usuarioNoExiste_retornaError() {
        EstudioDTO dto = new EstudioDTO();
        dto.setUserId("1");
        when(usuarioService.agregarEstudio("1", dto)).thenReturn(null);
        ResponseEntity<?> response = authController.actualizarEducacion(null, dto);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Usuario no encontrado", body.get("error"));
    }

    @Test
    void actualizarPerfil_usuarioExiste_retornaPerfil() {
        PerfilDTO dto = new PerfilDTO();
        Usuario usuario = new Usuario();
        when(usuarioService.actualizarPerfilPorEmail("test@test.com", dto)).thenReturn(usuario);
        ResponseEntity<?> response = authController.actualizarPerfil("test@test.com", dto);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(dto, body.get("perfil"));
    }

    @Test
    void actualizarPerfil_usuarioNoExiste_retornaError() {
        PerfilDTO dto = new PerfilDTO();
        when(usuarioService.actualizarPerfilPorEmail("no@existe.com", dto)).thenReturn(null);
        ResponseEntity<?> response = authController.actualizarPerfil("no@existe.com", dto);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Usuario no encontrado", body.get("error"));
    }
}
