package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.model.PostulacionId;
import com.empleos.web_empleos_backend.model.Usuario;
import com.empleos.web_empleos_backend.repository.PostulacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostulacionServiceTest {
    @Mock
    private PostulacionRepository postulacionRepository;
    @InjectMocks
    private PostulacionService postulacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_delegaEnRepositorio() {
        List<Postulacion> postulaciones = List.of(new Postulacion());
        when(postulacionRepository.findAll()).thenReturn(postulaciones);
        assertEquals(postulaciones, postulacionService.findAll());
    }

    @Test
    void findByEmail_delegaEnRepositorio() {
        List<Postulacion> todas = List.of(new Postulacion());
        List<Postulacion> filtradas = List.of(new Postulacion());
        when(postulacionRepository.findAll()).thenReturn(todas);
        when(postulacionRepository.findByUsuarioEmailIgnoreCase("correo@correo.com")).thenReturn(filtradas);
        List<Postulacion> result = postulacionService.findByEmail("correo@correo.com");
        assertEquals(filtradas, result);
    }

    @Test
    void findById_delegaEnRepositorio() {
        PostulacionId id = new PostulacionId(1L, "trab1");
        Postulacion postulacion = new Postulacion();
        when(postulacionRepository.findById(id)).thenReturn(Optional.of(postulacion));
        Optional<Postulacion> result = postulacionService.findById(1L, "trab1");
        assertTrue(result.isPresent());
    }

    @Test
    void save_delegaEnRepositorio() {
        Postulacion postulacion = new Postulacion();
        when(postulacionRepository.save(postulacion)).thenReturn(postulacion);
        Postulacion result = postulacionService.save(postulacion);
        assertEquals(postulacion, result);
    }

    @Test
    void deleteById_delegaEnRepositorio() {
        doNothing().when(postulacionRepository).deleteById(any(PostulacionId.class));
        postulacionService.deleteById(1L, "trab1");
        verify(postulacionRepository).deleteById(new PostulacionId(1L, "trab1"));
    }

    @Test
    void findByOfertaId_delegaEnRepositorio() {
        List<Postulacion> postulaciones = List.of(new Postulacion());
        when(postulacionRepository.findByOfertaId(1L)).thenReturn(postulaciones);
        List<Postulacion> result = postulacionService.findByOfertaId(1L);
        assertEquals(postulaciones, result);
    }

       @Test
    void findByEmail_usuarioNullYNoNull() {
        // Postulacion con usuario null
        Postulacion p1 = new Postulacion();
        p1.setOfertaId(1L);
        p1.setTrabajadorId("t1");
        p1.setEmail("a@email.com");
        p1.setUsuario(null);

        // Postulacion con usuario no null
        Postulacion p2 = new Postulacion();
        p2.setOfertaId(2L);
        p2.setTrabajadorId("t2");
        p2.setEmail("b@email.com");
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@email.com");
        p2.setUsuario(usuario);

        List<Postulacion> todas = List.of(p1, p2);
        List<Postulacion> resultList = List.of(p2);
        when(postulacionRepository.findAll()).thenReturn(todas);
        when(postulacionRepository.findByUsuarioEmailIgnoreCase("usuario@email.com")).thenReturn(resultList);

        List<Postulacion> result = postulacionService.findByEmail("usuario@email.com");
        assertEquals(1, result.size());
        assertEquals(p2, result.get(0));
    }

    @Test
    void save_usuarioNullYNoNull() {
        // Caso usuario null
        Postulacion p1 = new Postulacion();
        p1.setOfertaId(1L);
        p1.setTrabajadorId("t1");
        p1.setEmail("a@email.com");
        p1.setUsuario(null);
        when(postulacionRepository.save(p1)).thenReturn(p1);
        Postulacion result1 = postulacionService.save(p1);
        assertEquals(p1, result1);

        // Caso usuario no null
        Postulacion p2 = new Postulacion();
        p2.setOfertaId(2L);
        p2.setTrabajadorId("t2");
        p2.setEmail("b@email.com");
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@email.com");
        p2.setUsuario(usuario);
        when(postulacionRepository.save(p2)).thenReturn(p2);
        Postulacion result2 = postulacionService.save(p2);
        assertEquals(p2, result2);
    }
}
