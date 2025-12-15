package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactoTest {

    @Test
    void constructor_NoArgs_CreatesEmptyContacto() {
        Contacto contacto = new Contacto();
        assertNotNull(contacto);
        assertNull(contacto.getCelular());
        assertNull(contacto.getTelefono());
        assertNull(contacto.getEmail());
        assertNull(contacto.getDireccion());
    }

    @Test
    void constructor_AllArgs_SetsAllFields() {
        String celular = "+56912345678";
        String telefono = "+56223456789";
        String email = "contacto@email.com";
        String direccion = "Calle Falsa 123, Santiago";
        
        Contacto contacto = new Contacto(celular, telefono, email, direccion);
        
        assertEquals(celular, contacto.getCelular());
        assertEquals(telefono, contacto.getTelefono());
        assertEquals(email, contacto.getEmail());
        assertEquals(direccion, contacto.getDireccion());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        Contacto contacto = new Contacto();
        
        contacto.setCelular("+56987654321");
        assertEquals("+56987654321", contacto.getCelular());
        
        contacto.setTelefono("+56234567890");
        assertEquals("+56234567890", contacto.getTelefono());
        
        contacto.setEmail("nuevo@email.com");
        assertEquals("nuevo@email.com", contacto.getEmail());
        
        contacto.setDireccion("Avenida Principal 456, Valparaíso");
        assertEquals("Avenida Principal 456, Valparaíso", contacto.getDireccion());
    }

    @Test
    void equals_AndHashCode_WorkCorrectly() {
        Contacto contacto1 = new Contacto("+56912345678", "+56223456789", "test@email.com", "Dirección 1");
        Contacto contacto2 = new Contacto("+56912345678", "+56223456789", "test@email.com", "Dirección 1");
        Contacto contacto3 = new Contacto("+56987654321", "+56234567890", "otro@email.com", "Dirección 2");
        
        assertEquals(contacto1, contacto2);
        assertNotEquals(contacto1, contacto3);
        assertEquals(contacto1.hashCode(), contacto2.hashCode());
    }

    @Test
    void toString_WorksCorrectly() {
        Contacto contacto = new Contacto("+56912345678", "+56223456789", "test@email.com", "Santiago, Chile");
        
        String result = contacto.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("+56912345678"));
        assertTrue(result.contains("+56223456789"));
        assertTrue(result.contains("test@email.com"));
        assertTrue(result.contains("Santiago, Chile"));
    }

    @Test
    void embeddable_CanBeUsedInOtherEntities() {
        // Test that Contacto can be used as @Embedded
        Contacto contacto = new Contacto();
        contacto.setEmail("embedded@test.com");
        contacto.setCelular("+56999888777");
        
        // This would be used in Usuario entity
        Usuario usuario = new Usuario();
        usuario.setContacto(contacto);
        
        assertEquals(contacto, usuario.getContacto());
        assertEquals("embedded@test.com", usuario.getContacto().getEmail());
        assertEquals("+56999888777", usuario.getContacto().getCelular());
    }

    @Test
    void contactInfo_CanHandleNullValues() {
        Contacto contacto = new Contacto(null, null, null, null);
        
        assertNull(contacto.getCelular());
        assertNull(contacto.getTelefono());
        assertNull(contacto.getEmail());
        assertNull(contacto.getDireccion());
    }
}
