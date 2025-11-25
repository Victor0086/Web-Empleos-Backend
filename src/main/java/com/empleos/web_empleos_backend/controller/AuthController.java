package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.dto.EstudioDTO;
import com.empleos.web_empleos_backend.dto.ExperienciaDTO;
import com.empleos.web_empleos_backend.dto.PerfilDTO;
import com.empleos.web_empleos_backend.model.Estudio;
import com.empleos.web_empleos_backend.model.Experiencia;
import com.empleos.web_empleos_backend.model.Usuario;
import com.empleos.web_empleos_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
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


    @PutMapping("/educacion")
    public ResponseEntity<?> actualizarEducacion(@RequestParam String userId, @RequestBody EstudioDTO estudioDTO) {
        Estudio estudio = usuarioService.agregarEstudio(userId, estudioDTO);
        if (estudio == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of("educacion", estudio));
    }

    @PutMapping("/experiencia")
    public ResponseEntity<?> actualizarExperiencia(@RequestParam String userId, @RequestBody ExperienciaDTO experienciaDTO) {
        Experiencia experiencia = usuarioService.agregarExperiencia(userId, experienciaDTO);
        if (experiencia == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of("experiencia", experiencia));
    }

    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarPerfil(@RequestParam String email, @RequestBody PerfilDTO perfilDTO) {
        Usuario usuario = usuarioService.actualizarPerfilPorEmail(email, perfilDTO);
        if (usuario == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of("perfil", perfilDTO));
    }

    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> getPerfilByEmail(@RequestParam String email) {
        return usuarioService.buscarPorEmail(email)
            .map(usuario -> ResponseEntity.ok(Map.of("usuario", (Object) usuario)))
            .orElseGet(() -> ResponseEntity.ok(Map.of("error", "Usuario no encontrado")));
    }
    

    @PostMapping("/sync")
    public ResponseEntity<?> syncUser(@RequestBody Usuario usuario) {
        System.out.println("Recibido usuario para sincronizar: " + usuario);
        try {
            Usuario usuarioActualizado = usuarioService.registrarUsuario(usuario);
            System.out.println("Usuario sincronizado correctamente: " + usuarioActualizado);
            return ResponseEntity.ok(Map.of("usuario", usuarioActualizado));
        } catch (Exception e) {
            e.printStackTrace(); // Esto mostrará el error exacto en la consola del backend
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage())); // Esto enviará el mensaje al frontend
        }
    }
}
