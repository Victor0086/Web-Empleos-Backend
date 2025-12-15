package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Usuario;
import com.empleos.web_empleos_backend.model.Estudio;
import com.empleos.web_empleos_backend.model.Experiencia;
import com.empleos.web_empleos_backend.model.Role;
import com.empleos.web_empleos_backend.dto.PerfilDTO;
import com.empleos.web_empleos_backend.dto.ContactoDTO;
import com.empleos.web_empleos_backend.dto.EstudioDTO;
import com.empleos.web_empleos_backend.dto.ExperienciaDTO;
import com.empleos.web_empleos_backend.repository.UsuarioRepository;
import com.empleos.web_empleos_backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void emailExiste_true() {
        when(usuarioRepository.existsByEmail("correo@correo.com")).thenReturn(true);
        assertTrue(usuarioService.emailExiste("correo@correo.com"));
    }

    @Test
    void emailExiste_false() {
        when(usuarioRepository.existsByEmail("correo@correo.com")).thenReturn(false);
        assertFalse(usuarioService.emailExiste("correo@correo.com"));
    }

    @Test
    void registrarUsuario_nuevoUsuario_exito() {
        Usuario usuario = new Usuario();
        usuario.setEmail("correo@correo.com");
        when(usuarioRepository.findByEmail("correo@correo.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario result = usuarioService.registrarUsuario(usuario);
        assertEquals(usuario, result);
        assertNotNull(result.getFechaCreacion());
    }

    @Test
    void registrarUsuario_emailExistente_actualizaCampos() {
        Usuario existente = new Usuario();
        existente.setEmail("correo@correo.com");
        existente.setNombreCompleto("Antiguo");
        Usuario nuevo = new Usuario();
        nuevo.setEmail("correo@correo.com");
        nuevo.setNombreCompleto("Nuevo");
        when(usuarioRepository.findByEmail("correo@correo.com")).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);
        Usuario result = usuarioService.registrarUsuario(nuevo);
        assertEquals("Nuevo", result.getNombreCompleto());
    }

    @Test
    void registrarUsuario_emailNull_lanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setEmail(null);
        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrarUsuario(usuario));
    }

    @Test
    void login_devuelveUsuarioSiExiste() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findByEmail("correo@correo.com")).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = usuarioService.login("correo@correo.com", "pass");
        assertTrue(result.isPresent());
    }

    @Test
    void buscarPorEmail_devuelveUsuarioSiExiste() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findByEmail("correo@correo.com")).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = usuarioService.buscarPorEmail("correo@correo.com");
        assertTrue(result.isPresent());
    }

    @Test
    void buscarPorId_devuelveUsuarioSiExiste() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById("id1")).thenReturn(Optional.of(usuario));
        Usuario result = usuarioService.buscarPorId("id1");
        assertEquals(usuario, result);
    }

    @Test
    void guardar_delegaEnRepositorio() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario result = usuarioService.guardar(usuario);
        assertEquals(usuario, result);
    }

    @Test
    void agregarEstudio_usuarioExiste_agregaEstudio() {
        Usuario usuario = new Usuario();
        usuario.setEstudios(new ArrayList<>());
        when(usuarioRepository.findById("id1")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        EstudioDTO dto = new EstudioDTO();
        dto.setTitulo("titulo");
        Estudio result = usuarioService.agregarEstudio("id1", dto);
        assertEquals("titulo", result.getTitulo());
        assertEquals(1, usuario.getEstudios().size());
    }

    @Test
    void agregarEstudio_usuarioNoExiste_retornaNull() {
        when(usuarioRepository.findById("id1")).thenReturn(Optional.empty());
        EstudioDTO dto = new EstudioDTO();
        assertNull(usuarioService.agregarEstudio("id1", dto));
    }

    @Test
    void agregarExperiencia_usuarioExiste_agregaExperiencia() {
        Usuario usuario = new Usuario();
        usuario.setExperiencias(new ArrayList<>());
        when(usuarioRepository.findById("id1")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        ExperienciaDTO dto = new ExperienciaDTO();
        dto.setEmpresa("empresa");
        Experiencia result = usuarioService.agregarExperiencia("id1", dto);
        assertEquals("empresa", result.getEmpresa());
        assertEquals(1, usuario.getExperiencias().size());
    }

    @Test
    void agregarExperiencia_usuarioNoExiste_retornaNull() {
        when(usuarioRepository.findById("id1")).thenReturn(Optional.empty());
        ExperienciaDTO dto = new ExperienciaDTO();
        assertNull(usuarioService.agregarExperiencia("id1", dto));
    }

    @Test
    void actualizarPerfilPorEmail_actualizaCamposBasicosYContacto() {
        Usuario usuario = new Usuario();
        usuario.setEmail("correo@correo.com");
        when(usuarioRepository.findByEmail("correo@correo.com")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        PerfilDTO dto = new PerfilDTO();
        dto.setNombreCompleto("Nuevo Nombre");
        dto.setRol("ADMIN");
        dto.setFotoUrl("url");
        dto.setNacionalidad("CL");
        dto.setNacimiento(new java.sql.Date(new Date().getTime()));
        dto.setGenero("M");
        dto.setEstadoCivil("Soltero");
        dto.setLicencia("B");
        ContactoDTO contactoDTO = new ContactoDTO();
        contactoDTO.setCelular("123");
        dto.setContacto(contactoDTO);
        dto.setDescripcion("desc");
        dto.setHabilidades("Java");
        dto.setCvAdjunto("cv.pdf");
        dto.setResumenProfesional("resumen");
        Role role = new Role();
        when(roleRepository.findByNombre("ADMIN")).thenReturn(role);
        Usuario result = usuarioService.actualizarPerfilPorEmail("correo@correo.com", dto);
        assertEquals("Nuevo Nombre", result.getNombreCompleto());
        assertEquals(role, result.getRol());
        assertEquals("url", result.getFotoUrl());
        assertEquals("CL", result.getNacionalidad());
        assertEquals("M", result.getGenero());
        assertEquals("Soltero", result.getEstadoCivil());
        assertEquals("B", result.getLicencia());
        assertEquals("123", result.getContacto().getCelular());
        assertEquals("desc", result.getDescripcion());
        assertEquals("Java", result.getHabilidades());
        assertEquals("cv.pdf", result.getCvAdjunto());
        assertEquals("resumen", result.getResumenProfesional());
    }

    @Test
    void actualizarPerfilPorEmail_usuarioNoExiste_retornaNull() {
        when(usuarioRepository.findByEmail("correo@correo.com")).thenReturn(Optional.empty());
        PerfilDTO dto = new PerfilDTO();
        assertNull(usuarioService.actualizarPerfilPorEmail("correo@correo.com", dto));
    }
}
