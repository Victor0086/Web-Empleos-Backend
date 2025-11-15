package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Usuario;
import com.empleos.web_empleos_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario registrarUsuario(Usuario usuario) {
    // No se maneja contraseña aquí, solo se guarda el usuario
    return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> login(String email, String password) {
        // La autenticación se realiza por MSAL/Azure AD en el frontend
        // Aquí solo se puede buscar el usuario por email
        return usuarioRepository.findByEmail(email);
    }
}
