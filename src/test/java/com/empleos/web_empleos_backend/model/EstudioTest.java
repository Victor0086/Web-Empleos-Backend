package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EstudioTest {

    @Test
    void constructor_NoArgs_CreatesEmptyEstudio() {
        Estudio estudio = new Estudio();
        assertNotNull(estudio);
        assertNull(estudio.getId());
        assertNull(estudio.getTitulo());
    }

    @Test
    void constructor_AllArgs_SetsAllFields() {
        Long id = 1L;
        String titulo = "Ingeniería Informática";
        String institucion = "Universidad ABC";
        String tipo = "Pregrado";
        String estado = "Completado";
        String fechaInicio = "2020-03";
        String fechaFin = "2024-12";
        String referencia = "Diploma";
        Boolean certificado = true;
        Usuario usuario = new Usuario();
        
        Estudio estudio = new Estudio(id, titulo, institucion, tipo, estado, fechaInicio, fechaFin, referencia, certificado, usuario);
        
        assertEquals(id, estudio.getId());
        assertEquals(titulo, estudio.getTitulo());
        assertEquals(institucion, estudio.getInstitucion());
        assertEquals(tipo, estudio.getTipo());
        assertEquals(estado, estudio.getEstado());
        assertEquals(fechaInicio, estudio.getFechainicio());
        assertEquals(fechaFin, estudio.getFechaFin());
        assertEquals(referencia, estudio.getReferencia());
        assertEquals(certificado, estudio.getCertificado());
        assertEquals(usuario, estudio.getUsuario());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        Estudio estudio = new Estudio();
        
        estudio.setId(2L);
        assertEquals(2L, estudio.getId());
        
        estudio.setTitulo("Técnico en Programación");
        assertEquals("Técnico en Programación", estudio.getTitulo());
        
        estudio.setInstitucion("Instituto XYZ");
        assertEquals("Instituto XYZ", estudio.getInstitucion());
        
        estudio.setTipo("Técnico");
        assertEquals("Técnico", estudio.getTipo());
        
        estudio.setEstado("En curso");
        assertEquals("En curso", estudio.getEstado());
        
        estudio.setFechainicio("2023-03");
        assertEquals("2023-03", estudio.getFechainicio());
        
        estudio.setFechaFin("2024-12");
        assertEquals("2024-12", estudio.getFechaFin());
        
        estudio.setReferencia("Certificado");
        assertEquals("Certificado", estudio.getReferencia());
        
        estudio.setCertificado(false);
        assertEquals(false, estudio.getCertificado());
        
        Usuario usuario = new Usuario();
        estudio.setUsuario(usuario);
        assertEquals(usuario, estudio.getUsuario());
    }

    @Test
    void toString_WorksCorrectly() {
        Estudio estudio = new Estudio();
        estudio.setId(1L);
        estudio.setTitulo("Ingeniería en Sistemas");
        estudio.setInstitucion("Universidad DEF");
        estudio.setCertificado(true);
        
        Usuario usuario = new Usuario();
        usuario.setUserId("user123");
        estudio.setUsuario(usuario);
        
        String result = estudio.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("Ingeniería en Sistemas"));
        assertTrue(result.contains("Universidad DEF"));
        assertTrue(result.contains("user123"));
        assertTrue(result.contains("certificado=true"));
    }

    @Test
    void toString_WithNullUser_HandlesNull() {
        Estudio estudio = new Estudio();
        estudio.setId(1L);
        estudio.setTitulo("Título de prueba");
        estudio.setUsuario(null);
        
        String result = estudio.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("usuarioId=null"));
    }

    @Test
    void equals_AndHashCode_WorkCorrectly() {
        Estudio estudio1 = new Estudio();
        estudio1.setId(1L);
        estudio1.setTitulo("Título");
        
        Estudio estudio2 = new Estudio();
        estudio2.setId(1L);
        estudio2.setTitulo("Título");
        
        Estudio estudio3 = new Estudio();
        estudio3.setId(2L);
        estudio3.setTitulo("Otro título");
        
        assertEquals(estudio1, estudio2);
        assertNotEquals(estudio1, estudio3);
        assertEquals(estudio1.hashCode(), estudio2.hashCode());
    }
}
