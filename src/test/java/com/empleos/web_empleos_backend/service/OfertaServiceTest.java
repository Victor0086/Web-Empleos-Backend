package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Oferta;
import com.empleos.web_empleos_backend.repository.OfertaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfertaServiceTest {
    @Mock
    private OfertaRepository ofertaRepository;
    @InjectMocks
    private OfertaService ofertaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_delegaEnRepositorio() {
        List<Oferta> ofertas = List.of(new Oferta());
        when(ofertaRepository.findAll()).thenReturn(ofertas);
        assertEquals(ofertas, ofertaService.findAll());
    }

    @Test
    void findById_delegaEnRepositorio() {
        Oferta oferta = new Oferta();
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta));
        assertTrue(ofertaService.findById(1L).isPresent());
    }

    @Test
    void actualizarEstado_exito() {
        Oferta oferta = new Oferta();
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta));
        when(ofertaRepository.save(oferta)).thenReturn(oferta);
        Oferta result = ofertaService.actualizarEstado(1L, "CERRADA");
        assertEquals("CERRADA", oferta.getEstado());
        assertEquals(oferta, result);
    }

    @Test
    void actualizarEstado_ofertaNoExiste_lanzaExcepcion() {
        when(ofertaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ofertaService.actualizarEstado(1L, "CERRADA"));
    }

    @Test
    void save_estadoValidoYContratoValido_exito() {
        Oferta oferta = new Oferta();
        oferta.setEstado("ABIERTA");
        oferta.setContrato_type("INDEFINIDO");
        when(ofertaRepository.save(oferta)).thenReturn(oferta);
        Oferta result = ofertaService.save(oferta);
        assertEquals(oferta, result);
    }

    @Test
    void save_estadoInvalido_lanzaExcepcion() {
        Oferta oferta = new Oferta();
        oferta.setEstado("OTRO");
        oferta.setContrato_type("INDEFINIDO");
        assertThrows(IllegalArgumentException.class, () -> ofertaService.save(oferta));
    }

    @Test
    void save_contratoTypeInvalido_lanzaExcepcion() {
        Oferta oferta = new Oferta();
        oferta.setEstado("ABIERTA");
        oferta.setContrato_type("OTRO");
        assertThrows(IllegalArgumentException.class, () -> ofertaService.save(oferta));
    }

    @Test
    void deleteById_delegaEnRepositorio() {
        doNothing().when(ofertaRepository).deleteById(1L);
        ofertaService.deleteById(1L);
        verify(ofertaRepository).deleteById(1L);
    }

    @Test
    void findByEmpleadorId_delegaEnRepositorio() {
        List<Oferta> ofertas = List.of(new Oferta());
        when(ofertaRepository.findByEmpleadorId("emp1")).thenReturn(ofertas);
        assertEquals(ofertas, ofertaService.findByEmpleadorId("emp1"));
    }
}
