package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class ContratoTest {

    @Test
    void constructor_NoArgs_CreatesEmptyContract() {
        Contrato contrato = new Contrato();
        assertNotNull(contrato);
        assertNull(contrato.getId());
        assertNull(contrato.getPostulacion());
        assertNull(contrato.getEstado());
    }

    @Test
    void constructor_WithPostulacion_SetsPostulacion() {
        Postulacion postulacion = new Postulacion();
        Contrato contrato = new Contrato(postulacion);
        
        assertEquals(postulacion, contrato.getPostulacion());
        assertNull(contrato.getId());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        Contrato contrato = new Contrato();
        
        contrato.setId(1L);
        assertEquals(1L, contrato.getId());
        
        Postulacion postulacion = new Postulacion();
        contrato.setPostulacion(postulacion);
        assertEquals(postulacion, contrato.getPostulacion());
        
        contrato.setIdNotario("notario123");
        assertEquals("notario123", contrato.getIdNotario());
        
        contrato.setEstado("ACTIVO");
        assertEquals("ACTIVO", contrato.getEstado());
        
        LocalDateTime fechaTrabajador = LocalDateTime.now();
        contrato.setFirmaTrabajador(fechaTrabajador);
        assertEquals(fechaTrabajador, contrato.getFirmaTrabajador());
        
        LocalDateTime fechaEmpleador = LocalDateTime.now();
        contrato.setFirmaEmpleador(fechaEmpleador);
        assertEquals(fechaEmpleador, contrato.getFirmaEmpleador());
        
        LocalDateTime fechaNotario = LocalDateTime.now();
        contrato.setFirmaNotario(fechaNotario);
        assertEquals(fechaNotario, contrato.getFirmaNotario());
        
        LocalDateTime fechaCreacion = LocalDateTime.now();
        contrato.setFechaCreacion(fechaCreacion);
        assertEquals(fechaCreacion, contrato.getFechaCreacion());
    }

    @Test
    void onCreate_SetsDefaults() {
        Contrato contrato = new Contrato();
        
        // Simulate @PrePersist call
        contrato.onCreate();
        
        assertNotNull(contrato.getFechaCreacion());
        assertEquals("PENDIENTE_FIRMAS", contrato.getEstado());
    }

    @Test
    void onCreate_DoesNotOverrideExistingState() {
        Contrato contrato = new Contrato();
        contrato.setEstado("ACTIVO");
        
        // Simulate @PrePersist call
        contrato.onCreate();
        
        assertNotNull(contrato.getFechaCreacion());
        assertEquals("ACTIVO", contrato.getEstado());
    }

    @Test
    void allStates_CanBeSet() {
        Contrato contrato = new Contrato();
        
        String[] estados = {"PENDIENTE_FIRMAS", "ACTIVO", "FINALIZADO", "CANCELADO", "RECHAZADA"};
        
        for (String estado : estados) {
            contrato.setEstado(estado);
            assertEquals(estado, contrato.getEstado());
        }
    }

    @Test
    void firmas_CanBeSetIndependently() {
        Contrato contrato = new Contrato();
        
        LocalDateTime now = LocalDateTime.now();
        
        contrato.setFirmaTrabajador(now);
        assertEquals(now, contrato.getFirmaTrabajador());
        assertNull(contrato.getFirmaEmpleador());
        assertNull(contrato.getFirmaNotario());
        
        contrato.setFirmaEmpleador(now.plusMinutes(5));
        assertEquals(now, contrato.getFirmaTrabajador());
        assertEquals(now.plusMinutes(5), contrato.getFirmaEmpleador());
        assertNull(contrato.getFirmaNotario());
        
        contrato.setFirmaNotario(now.plusMinutes(10));
        assertEquals(now, contrato.getFirmaTrabajador());
        assertEquals(now.plusMinutes(5), contrato.getFirmaEmpleador());
        assertEquals(now.plusMinutes(10), contrato.getFirmaNotario());
    }
}