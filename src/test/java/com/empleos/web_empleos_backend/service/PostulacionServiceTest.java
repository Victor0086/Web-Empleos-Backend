package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.model.PostulacionId;
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
}
