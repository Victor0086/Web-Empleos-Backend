package com.empleos.web_empleos_backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    void constructor_NoArgs_CreatesEmptyRole() {
        Role role = new Role();
        assertNotNull(role);
        assertNull(role.getId());
        assertNull(role.getNombre());
    }

    @Test
    void constructor_AllArgs_SetsAllFields() {
        Integer id = 1;
        String nombre = "ADMINISTRADOR";
        
        Role role = new Role(id, nombre);
        
        assertEquals(id, role.getId());
        assertEquals(nombre, role.getNombre());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        Role role = new Role();
        
        role.setId(2);
        assertEquals(2, role.getId());
        
        role.setNombre("EMPLEADOR");
        assertEquals("EMPLEADOR", role.getNombre());
    }

    @Test
    void roleNames_CanBeSet() {
        Role role = new Role();
        
        String[] nombres = {"ADMINISTRADOR", "EMPLEADOR", "TRABAJADOR", "NOTARIO"};
        
        for (String nombre : nombres) {
            role.setNombre(nombre);
            assertEquals(nombre, role.getNombre());
        }
    }

    @Test
    void equals_AndHashCode_WorkCorrectly() {
        Role role1 = new Role(1, "ADMINISTRADOR");
        Role role2 = new Role(1, "ADMINISTRADOR");
        Role role3 = new Role(2, "EMPLEADOR");
        
        assertEquals(role1, role2);
        assertNotEquals(role1, role3);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void toString_WorksCorrectly() {
        Role role = new Role(1, "ADMINISTRADOR");
        
        String result = role.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("ADMINISTRADOR"));
        assertTrue(result.contains("1"));
    }
}
