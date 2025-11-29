package com.empleos.web_empleos_backend.repository;

import com.empleos.web_empleos_backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByNombre(String nombre);
}
