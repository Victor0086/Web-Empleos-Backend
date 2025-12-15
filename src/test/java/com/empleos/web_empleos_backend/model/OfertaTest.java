package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class OfertaTest {

    @Test
    void constructor_NoArgs_CreatesEmptyOferta() {
        Oferta oferta = new Oferta();
        assertNotNull(oferta);
        assertNull(oferta.getOferta_id());
        assertNull(oferta.getEmpleadorId());
    }

    @Test
    void constructor_AllArgs_SetsAllFields() {
        Long ofertaId = 1L;
        String empleadorId = "emp123";
        String titulo = "Desarrollador Java";
        String descripcion = "Desarrollo de aplicaciones";
        String contratoType = "Tiempo completo";
        String location = "Buenos Aires";
        String estado = "ACTIVA";
        Date fechaCreacion = new Date();
        Date fechaModificacion = new Date();

        Oferta oferta = new Oferta(ofertaId, empleadorId, titulo, descripcion, 
                                   contratoType, location, estado, fechaCreacion, fechaModificacion);

        assertEquals(ofertaId, oferta.getOferta_id());
        assertEquals(empleadorId, oferta.getEmpleadorId());
        assertEquals(titulo, oferta.getTitulo());
        assertEquals(descripcion, oferta.getDescripcion());
        assertEquals(contratoType, oferta.getContrato_type());
        assertEquals(location, oferta.getLocation());
        assertEquals(estado, oferta.getEstado());
        assertEquals(fechaCreacion, oferta.getFecha_creacion());
        assertEquals(fechaModificacion, oferta.getFecha_modificacion());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        Oferta oferta = new Oferta();
        
        oferta.setOferta_id(2L);
        assertEquals(2L, oferta.getOferta_id());
        
        oferta.setEmpleadorId("emp456");
        assertEquals("emp456", oferta.getEmpleadorId());
        
        oferta.setTitulo("Analista de Sistemas");
        assertEquals("Analista de Sistemas", oferta.getTitulo());
        
        oferta.setDescripcion("Análisis y desarrollo de sistemas");
        assertEquals("Análisis y desarrollo de sistemas", oferta.getDescripcion());
        
        oferta.setContrato_type("Medio tiempo");
        assertEquals("Medio tiempo", oferta.getContrato_type());
        
        oferta.setLocation("Córdoba");
        assertEquals("Córdoba", oferta.getLocation());
        
        oferta.setEstado("INACTIVA");
        assertEquals("INACTIVA", oferta.getEstado());
        
        Date fechaCreacion = new Date();
        oferta.setFecha_creacion(fechaCreacion);
        assertEquals(fechaCreacion, oferta.getFecha_creacion());
        
        Date fechaModificacion = new Date();
        oferta.setFecha_modificacion(fechaModificacion);
        assertEquals(fechaModificacion, oferta.getFecha_modificacion());
    }

    @Test
    void equals_AndHashCode_WorkCorrectly() {
        Oferta oferta1 = new Oferta();
        oferta1.setOferta_id(1L);
        oferta1.setTitulo("Desarrollador");
        
        Oferta oferta2 = new Oferta();
        oferta2.setOferta_id(1L);
        oferta2.setTitulo("Desarrollador");
        
        Oferta oferta3 = new Oferta();
        oferta3.setOferta_id(2L);
        oferta3.setTitulo("Analista");
        
        assertEquals(oferta1, oferta2);
        assertNotEquals(oferta1, oferta3);
        assertEquals(oferta1.hashCode(), oferta2.hashCode());
    }

    @Test
    void toString_WorksCorrectly() {
        Oferta oferta = new Oferta();
        oferta.setOferta_id(1L);
        oferta.setTitulo("Desarrollador Java");
        oferta.setEmpleadorId("emp123");
        
        String result = oferta.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("Desarrollador Java"));
        assertTrue(result.contains("emp123"));
    }
}