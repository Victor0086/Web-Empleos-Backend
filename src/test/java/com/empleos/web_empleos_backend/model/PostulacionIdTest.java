package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostulacionIdTest {

    @Test
    void constructor_NoArgs_CreatesEmptyId() {
        PostulacionId id = new PostulacionId();
        assertNotNull(id);
        assertNull(id.getOfertaId());
        assertNull(id.getTrabajadorId());
    }

    @Test
    void constructor_WithParams_SetsFields() {
        Long ofertaId = 1L;
        String trabajadorId = "trab123";
        
        PostulacionId id = new PostulacionId(ofertaId, trabajadorId);
        
        assertEquals(ofertaId, id.getOfertaId());
        assertEquals(trabajadorId, id.getTrabajadorId());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        PostulacionId id = new PostulacionId();
        
        id.setOfertaId(2L);
        assertEquals(2L, id.getOfertaId());
        
        id.setTrabajadorId("trab456");
        assertEquals("trab456", id.getTrabajadorId());
    }

    @Test
    void equals_WorksCorrectly() {
        PostulacionId id1 = new PostulacionId(1L, "trab123");
        PostulacionId id2 = new PostulacionId(1L, "trab123");
        PostulacionId id3 = new PostulacionId(2L, "trab456");
        PostulacionId id4 = new PostulacionId(1L, "trab456");
        PostulacionId id5 = new PostulacionId(2L, "trab123");

        // Same values
        assertEquals(id1, id2);
        assertEquals(id2, id1);
        
        // Different values
        assertNotEquals(id1, id3);
        assertNotEquals(id1, id4);
        assertNotEquals(id1, id5);
        
        // Self equality
        assertEquals(id1, id1);
        
        // Null comparison
        assertNotEquals(id1, null);
        
        // Different class
        assertNotEquals(id1, "string");
    }

    @Test
    void hashCode_WorksCorrectly() {
        PostulacionId id1 = new PostulacionId(1L, "trab123");
        PostulacionId id2 = new PostulacionId(1L, "trab123");
        PostulacionId id3 = new PostulacionId(2L, "trab456");

        // Equal objects have equal hash codes
        assertEquals(id1.hashCode(), id2.hashCode());
        
        // Different objects should have different hash codes (usually)
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void equals_WithNullFields() {
        PostulacionId id1 = new PostulacionId(null, null);
        PostulacionId id2 = new PostulacionId(null, null);
        PostulacionId id3 = new PostulacionId(1L, null);
        PostulacionId id4 = new PostulacionId(null, "trab123");

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertNotEquals(id1, id4);
        assertNotEquals(id3, id4);
    }

    @Test
    void hashCode_WithNullFields() {
        PostulacionId id1 = new PostulacionId(null, null);
        PostulacionId id2 = new PostulacionId(null, null);

        assertEquals(id1.hashCode(), id2.hashCode());
    }
}