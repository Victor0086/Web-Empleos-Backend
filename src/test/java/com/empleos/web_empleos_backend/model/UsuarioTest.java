package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class UsuarioTest {

    @Test
    void constructor_NoArgs_CreatesEmptyUser() {
        Usuario usuario = new Usuario();
        assertNotNull(usuario);
        assertNull(usuario.getUserId());
        assertNull(usuario.getEmail());
    }

    @Test
    void constructor_AllArgs_SetsAllFields() {
        Role role = new Role();
        Date fechaCreacion = new Date();
        Date nacimiento = new Date();
        Contacto contacto = new Contacto();
        List<Estudio> estudios = new ArrayList<>();
        List<Experiencia> experiencias = new ArrayList<>();
        List<Postulacion> postulaciones = new ArrayList<>();

        Usuario usuario = new Usuario("user123", "test@email.com", "Juan Pérez", role, 
                                      fechaCreacion, "foto.jpg", "Argentina", nacimiento, 
                                      "Masculino", "Soltero", "B", contacto, estudios, 
                                      experiencias, postulaciones, "Descripción", "Java, Spring", 
                                      "cv.pdf", "Resumen profesional");

        assertEquals("user123", usuario.getUserId());
        assertEquals("test@email.com", usuario.getEmail());
        assertEquals("Juan Pérez", usuario.getNombreCompleto());
        assertEquals(role, usuario.getRol());
        assertEquals(fechaCreacion, usuario.getFechaCreacion());
        assertEquals("foto.jpg", usuario.getFotoUrl());
        assertEquals("Argentina", usuario.getNacionalidad());
        assertEquals(nacimiento, usuario.getNacimiento());
        assertEquals("Masculino", usuario.getGenero());
        assertEquals("Soltero", usuario.getEstadoCivil());
        assertEquals("B", usuario.getLicencia());
        assertEquals(contacto, usuario.getContacto());
        assertEquals(estudios, usuario.getEstudios());
        assertEquals(experiencias, usuario.getExperiencias());
        assertEquals(postulaciones, usuario.getPostulaciones());
        assertEquals("Descripción", usuario.getDescripcion());
        assertEquals("Java, Spring", usuario.getHabilidades());
        assertEquals("cv.pdf", usuario.getCvAdjunto());
        assertEquals("Resumen profesional", usuario.getResumenProfesional());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        Usuario usuario = new Usuario();
        
        usuario.setUserId("user456");
        assertEquals("user456", usuario.getUserId());
        
        usuario.setEmail("nuevo@email.com");
        assertEquals("nuevo@email.com", usuario.getEmail());
        
        usuario.setNombreCompleto("María González");
        assertEquals("María González", usuario.getNombreCompleto());
        
        Role role = new Role();
        usuario.setRol(role);
        assertEquals(role, usuario.getRol());
        
        Date fecha = new Date();
        usuario.setFechaCreacion(fecha);
        assertEquals(fecha, usuario.getFechaCreacion());
        
        usuario.setFotoUrl("nueva-foto.jpg");
        assertEquals("nueva-foto.jpg", usuario.getFotoUrl());
        
        usuario.setNacionalidad("Colombia");
        assertEquals("Colombia", usuario.getNacionalidad());
        
        usuario.setNacimiento(fecha);
        assertEquals(fecha, usuario.getNacimiento());
        
        usuario.setGenero("Femenino");
        assertEquals("Femenino", usuario.getGenero());
        
        usuario.setEstadoCivil("Casado");
        assertEquals("Casado", usuario.getEstadoCivil());
        
        usuario.setLicencia("A");
        assertEquals("A", usuario.getLicencia());
        
        Contacto contacto = new Contacto();
        usuario.setContacto(contacto);
        assertEquals(contacto, usuario.getContacto());
        
        List<Estudio> estudios = new ArrayList<>();
        usuario.setEstudios(estudios);
        assertEquals(estudios, usuario.getEstudios());
        
        List<Experiencia> experiencias = new ArrayList<>();
        usuario.setExperiencias(experiencias);
        assertEquals(experiencias, usuario.getExperiencias());
        
        List<Postulacion> postulaciones = new ArrayList<>();
        usuario.setPostulaciones(postulaciones);
        assertEquals(postulaciones, usuario.getPostulaciones());
        
        usuario.setDescripcion("Nueva descripción");
        assertEquals("Nueva descripción", usuario.getDescripcion());
        
        usuario.setHabilidades("Python, Django");
        assertEquals("Python, Django", usuario.getHabilidades());
        
        usuario.setCvAdjunto("nuevo-cv.pdf");
        assertEquals("nuevo-cv.pdf", usuario.getCvAdjunto());
        
        usuario.setResumenProfesional("Nuevo resumen");
        assertEquals("Nuevo resumen", usuario.getResumenProfesional());
    }

    @Test
    void toString_ReturnsCorrectFormat() {
        Usuario usuario = new Usuario();
        usuario.setUserId("user123");
        usuario.setEmail("test@email.com");
        usuario.setNombreCompleto("Juan Pérez");
        
        String result = usuario.toString();
        
        assertTrue(result.contains("user123"));
        assertTrue(result.contains("test@email.com"));
        assertTrue(result.contains("Juan Pérez"));
        assertTrue(result.startsWith("Usuario{"));
        assertTrue(result.contains("estudiosCount=0"));
        assertTrue(result.contains("experienciasCount=0"));
    }

    @Test
    void toString_WithCollections_ShowsCorrectCounts() {
        Usuario usuario = new Usuario();
        usuario.setUserId("user123");
        
        List<Estudio> estudios = new ArrayList<>();
        estudios.add(new Estudio());
        estudios.add(new Estudio());
        usuario.setEstudios(estudios);
        
        List<Experiencia> experiencias = new ArrayList<>();
        experiencias.add(new Experiencia());
        usuario.setExperiencias(experiencias);
        
        String result = usuario.toString();
        
        assertTrue(result.contains("estudiosCount=2"));
        assertTrue(result.contains("experienciasCount=1"));
    }

    @Test
    void equals_AndHashCode_WorkCorrectly() {
        Usuario usuario1 = new Usuario();
        usuario1.setUserId("user123");
        usuario1.setEmail("test@email.com");
        
        Usuario usuario2 = new Usuario();
        usuario2.setUserId("user123");
        usuario2.setEmail("test@email.com");
        
        Usuario usuario3 = new Usuario();
        usuario3.setUserId("user456");
        usuario3.setEmail("otro@email.com");
        
        assertEquals(usuario1, usuario2);
        assertNotEquals(usuario1, usuario3);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }
}