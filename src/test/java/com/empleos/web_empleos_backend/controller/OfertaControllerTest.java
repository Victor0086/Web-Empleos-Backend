package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Oferta;
import com.empleos.web_empleos_backend.service.OfertaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfertaControllerTest {
    @Mock
    private OfertaService ofertaService;
    @InjectMocks
    private OfertaController ofertaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_retornaListaOfertas() {
        List<Oferta> ofertas = List.of(new Oferta(), new Oferta());
        when(ofertaService.findAll()).thenReturn(ofertas);
        List<Oferta> result = ofertaController.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void actualizarEstado_retornaOfertaActualizada() {
        Oferta oferta = new Oferta();
        Map<String, String> body = Map.of("estado", "CERRADA");
        when(ofertaService.actualizarEstado(1L, "CERRADA")).thenReturn(oferta);
        ResponseEntity<Oferta> response = ofertaController.actualizarEstado(1L, body);
        assertEquals(oferta, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getById_ofertaExiste_retornaOferta() {
        Oferta oferta = new Oferta();
        when(ofertaService.findById(1L)).thenReturn(Optional.of(oferta));
        ResponseEntity<Oferta> response = ofertaController.getById(1L);
        assertEquals(oferta, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getById_ofertaNoExiste_retornaNotFound() {
        when(ofertaService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Oferta> response = ofertaController.getById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void create_retornaOfertaGuardada() {
        Oferta oferta = new Oferta();
        when(ofertaService.save(oferta)).thenReturn(oferta);
        Oferta result = ofertaController.create(oferta);
        assertEquals(oferta, result);
    }

    @Test
    void delete_eliminaOferta() {
        doNothing().when(ofertaService).deleteById(1L);
        ResponseEntity<Void> response = ofertaController.delete(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void getByEmpleadorId_retornaOfertas() {
        List<Oferta> ofertas = List.of(new Oferta());
        when(ofertaService.findByEmpleadorId("emp1")).thenReturn(ofertas);
        List<Oferta> result = ofertaController.getByEmpleadorId("emp1");
        assertEquals(1, result.size());
    }
}
