package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Usuario;
import com.empleos.web_empleos_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private com.empleos.web_empleos_backend.service.JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuarioService.emailExiste(usuario.getEmail())) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "El email ya está registrado");
            return ResponseEntity.badRequest().body(error);
        }
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("usuario", nuevoUsuario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        return usuarioService.login(email, password)
            .map(usuario -> {
                String token = jwtService.generateToken(email);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("usuario", usuario);
                response.put("token", token);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Credenciales incorrectas");
                return ResponseEntity.badRequest().body(error);
            });
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        return usuarioService.buscarPorEmail(email)
            .map(usuario -> {
                Map<String, Object> response = new HashMap<>();
                response.put("usuario", usuario);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Usuario no encontrado");
                return ResponseEntity.ok(error); // No error 404, para que el frontend muestre el formulario
            });
    }
}
