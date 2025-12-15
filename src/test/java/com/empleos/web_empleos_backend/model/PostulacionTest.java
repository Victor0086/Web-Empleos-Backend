package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class PostulacionTest {

    @Test
    void constructor_NoArgs_CreatesPostulacionWithDefaults() {
        Postulacion postulacion = new Postulacion();
        assertNotNull(postulacion);
        assertEquals("pendiente", postulacion.getEstado());
        assertNotNull(postulacion.getFecha_postulacion());
    }

    @Test
    void constructor_AllArgs_SetsAllFields() {
        Long ofertaId = 1L;
        String trabajadorId = "trab123";
        String nombres = "Juan";
        String apellidos = "Pérez";
        String rut = "12345678-9";
        String telefono = "+56912345678";
        String email = "juan@email.com";
        String experiencia = "2 años";
        String descripcionExperiencia = "Desarrollo web";
        String motivacion = "Crecimiento profesional";
        String curriculumPath = "/uploads/cv.pdf";
        String curriculumName = "cv.pdf";
        Usuario usuario = new Usuario();
        String estado = "aceptada";
        Date fechaPostulacion = new Date();

        Postulacion postulacion = new Postulacion(ofertaId, trabajadorId, nombres, apellidos, rut, telefono, 
                                                  email, experiencia, descripcionExperiencia, motivacion, 
                                                  curriculumPath, curriculumName, usuario, estado, fechaPostulacion);

        assertEquals(ofertaId, postulacion.getOfertaId());
        assertEquals(trabajadorId, postulacion.getTrabajadorId());
        assertEquals(nombres, postulacion.getNombres());
        assertEquals(apellidos, postulacion.getApellidos());
        assertEquals(rut, postulacion.getRut());
        assertEquals(telefono, postulacion.getTelefono());
        assertEquals(email, postulacion.getEmail());
        assertEquals(experiencia, postulacion.getExperiencia());
        assertEquals(descripcionExperiencia, postulacion.getDescripcionExperiencia());
        assertEquals(motivacion, postulacion.getMotivacion());
        assertEquals(curriculumPath, postulacion.getCurriculumPath());
        assertEquals(curriculumName, postulacion.getCurriculumName());
        assertEquals(usuario, postulacion.getUsuario());
        assertEquals(estado, postulacion.getEstado());
        assertEquals(fechaPostulacion, postulacion.getFecha_postulacion());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        Postulacion postulacion = new Postulacion();
        
        postulacion.setOfertaId(2L);
        assertEquals(2L, postulacion.getOfertaId());
        
        postulacion.setTrabajadorId("trab456");
        assertEquals("trab456", postulacion.getTrabajadorId());
        
        postulacion.setNombres("María");
        assertEquals("María", postulacion.getNombres());
        
        postulacion.setApellidos("González");
        assertEquals("González", postulacion.getApellidos());
        
        postulacion.setRut("98765432-1");
        assertEquals("98765432-1", postulacion.getRut());
        
        postulacion.setTelefono("+56987654321");
        assertEquals("+56987654321", postulacion.getTelefono());
        
        postulacion.setEmail("maria@email.com");
        assertEquals("maria@email.com", postulacion.getEmail());
        
        postulacion.setExperiencia("5 años");
        assertEquals("5 años", postulacion.getExperiencia());
        
        postulacion.setDescripcionExperiencia("Desarrollo móvil");
        assertEquals("Desarrollo móvil", postulacion.getDescripcionExperiencia());
        
        postulacion.setMotivacion("Nuevos desafíos");
        assertEquals("Nuevos desafíos", postulacion.getMotivacion());
        
        postulacion.setCurriculumPath("/uploads/maria_cv.pdf");
        assertEquals("/uploads/maria_cv.pdf", postulacion.getCurriculumPath());
        
        postulacion.setCurriculumName("maria_cv.pdf");
        assertEquals("maria_cv.pdf", postulacion.getCurriculumName());
        
        Usuario usuario = new Usuario();
        postulacion.setUsuario(usuario);
        assertEquals(usuario, postulacion.getUsuario());
        
        postulacion.setEstado("rechazada");
        assertEquals("rechazada", postulacion.getEstado());
        
        Date nuevaFecha = new Date();
        postulacion.setFecha_postulacion(nuevaFecha);
        assertEquals(nuevaFecha, postulacion.getFecha_postulacion());
    }

    @Test
    void estadosPostulacion_CanBeSet() {
        Postulacion postulacion = new Postulacion();
        
        String[] estados = {"pendiente", "aceptada", "rechazada", "CONTRATO_FIRMADO"};
        
        for (String estado : estados) {
            postulacion.setEstado(estado);
            assertEquals(estado, postulacion.getEstado());
        }
    }

    @Test
    void equals_AndHashCode_WorkCorrectly() {
        Postulacion postulacion1 = new Postulacion();
        postulacion1.setOfertaId(1L);
        postulacion1.setTrabajadorId("trab123");
        postulacion1.setEmail("test@email.com");
        
        Postulacion postulacion2 = new Postulacion();
        postulacion2.setOfertaId(1L);
        postulacion2.setTrabajadorId("trab123");
        postulacion2.setEmail("test@email.com");
        
        Postulacion postulacion3 = new Postulacion();
        postulacion3.setOfertaId(2L);
        postulacion3.setTrabajadorId("trab456");
        postulacion3.setEmail("otro@email.com");
        
        assertEquals(postulacion1, postulacion2);
        assertNotEquals(postulacion1, postulacion3);
        assertEquals(postulacion1.hashCode(), postulacion2.hashCode());
    }

    @Test
    void toString_WorksCorrectly() {
        Postulacion postulacion = new Postulacion();
        postulacion.setOfertaId(1L);
        postulacion.setTrabajadorId("trab123");
        postulacion.setNombres("Juan");
        postulacion.setApellidos("Pérez");
        postulacion.setEmail("juan@email.com");
        
        String result = postulacion.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("Juan"));
        assertTrue(result.contains("Pérez"));
        assertTrue(result.contains("juan@email.com"));
    }
}