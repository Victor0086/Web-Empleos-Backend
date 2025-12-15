package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExperienciaTest {

    @Test
    void constructor_NoArgs_CreatesEmptyExperiencia() {
        Experiencia experiencia = new Experiencia();
        assertNotNull(experiencia);
        assertNull(experiencia.getId());
        assertNull(experiencia.getEmpresa());
    }

    @Test
    void constructor_AllArgs_SetsAllFields() {
        Long id = 1L;
        String empresa = "Tech Solutions";
        String puesto = "Desarrollador Java";
        String fechaInicio = "2022-01";
        String fechaFin = "2023-12";
        String descripcion = "Desarrollo de aplicaciones web";
        Usuario usuario = new Usuario();
        
        Experiencia experiencia = new Experiencia(id, empresa, puesto, fechaInicio, fechaFin, descripcion, usuario);
        
        assertEquals(id, experiencia.getId());
        assertEquals(empresa, experiencia.getEmpresa());
        assertEquals(puesto, experiencia.getPuesto());
        assertEquals(fechaInicio, experiencia.getFechaInicio());
        assertEquals(fechaFin, experiencia.getFechaFin());
        assertEquals(descripcion, experiencia.getDescripcion());
        assertEquals(usuario, experiencia.getUsuario());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        Experiencia experiencia = new Experiencia();
        
        experiencia.setId(2L);
        assertEquals(2L, experiencia.getId());
        
        experiencia.setEmpresa("Innovative Corp");
        assertEquals("Innovative Corp", experiencia.getEmpresa());
        
        experiencia.setPuesto("Senior Developer");
        assertEquals("Senior Developer", experiencia.getPuesto());
        
        experiencia.setFechaInicio("2021-06");
        assertEquals("2021-06", experiencia.getFechaInicio());
        
        experiencia.setFechaFin("2024-03");
        assertEquals("2024-03", experiencia.getFechaFin());
        
        experiencia.setDescripcion("Liderazgo de equipo y desarrollo de arquitectura");
        assertEquals("Liderazgo de equipo y desarrollo de arquitectura", experiencia.getDescripcion());
        
        Usuario usuario = new Usuario();
        experiencia.setUsuario(usuario);
        assertEquals(usuario, experiencia.getUsuario());
    }

    @Test
    void toString_WorksCorrectly_ExcludesUser() {
        Experiencia experiencia = new Experiencia();
        experiencia.setId(1L);
        experiencia.setEmpresa("Test Company");
        experiencia.setPuesto("QA Engineer");
        
        Usuario usuario = new Usuario();
        usuario.setUserId("user123");
        experiencia.setUsuario(usuario);
        
        String result = experiencia.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("Test Company"));
        assertTrue(result.contains("QA Engineer"));
        // Due to @ToString(exclude = "usuario"), user should not be in toString
        assertFalse(result.contains("user123"));
    }

    @Test
    void equals_AndHashCode_WorkCorrectly() {
        Experiencia exp1 = new Experiencia();
        exp1.setId(1L);
        exp1.setEmpresa("Company A");
        exp1.setPuesto("Developer");
        
        Experiencia exp2 = new Experiencia();
        exp2.setId(1L);
        exp2.setEmpresa("Company A");
        exp2.setPuesto("Developer");
        
        Experiencia exp3 = new Experiencia();
        exp3.setId(2L);
        exp3.setEmpresa("Company B");
        exp3.setPuesto("Tester");
        
        assertEquals(exp1, exp2);
        assertNotEquals(exp1, exp3);
        assertEquals(exp1.hashCode(), exp2.hashCode());
    }

    @Test
    void currentJob_CanBeRepresented() {
        Experiencia experiencia = new Experiencia();
        experiencia.setEmpresa("Current Company");
        experiencia.setPuesto("Current Position");
        experiencia.setFechaInicio("2023-01");
        experiencia.setFechaFin(null); // Current job has no end date
        
        assertEquals("Current Company", experiencia.getEmpresa());
        assertEquals("Current Position", experiencia.getPuesto());
        assertEquals("2023-01", experiencia.getFechaInicio());
        assertNull(experiencia.getFechaFin());
    }
}
