package com.empleos.web_empleos_backend.repository;

import com.empleos.web_empleos_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
}
