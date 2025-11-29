package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.dto.EstudioDTO;
import com.empleos.web_empleos_backend.dto.ExperienciaDTO;
import com.empleos.web_empleos_backend.dto.PerfilDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public ResponseEntity<?> actualizarEducacion(@RequestParam(required = false) String userId, @RequestBody EstudioDTO estudioDTO) {
        // Si no viene como query param, tomarlo del body
        if (userId == null || userId.isEmpty()) {
            userId = estudioDTO.getUserId();
        }
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Falta el userId"));
        }
        Estudio estudio = usuarioService.agregarEstudio(userId, estudioDTO);
        if (estudio == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of("educacion", estudio));
    }

    @SuppressWarnings("unchecked")
    @PutMapping("/experiencia")
    public ResponseEntity<?> actualizarExperiencia(@RequestParam(required = false) String userId, @RequestBody Object body) {
        ObjectMapper mapper = new ObjectMapper();
        // Si es un array, procesar cada experiencia
        if (body instanceof java.util.List) {
            java.util.List<?> experienciasList = (java.util.List<?>) body;
            java.util.List<Object> resultados = new java.util.ArrayList<>();
            for (Object obj : experienciasList) {
                if (!(obj instanceof java.util.Map)) {
                    resultados.add(Map.of("error", "Cada experiencia debe ser un objeto JSON", "data", obj));
                    continue;
                }
                java.util.Map<String, Object> experienciaMap = (java.util.Map<String, Object>) obj;
                String uid = userId;
                if (uid == null) {
                    Object userIdObj = experienciaMap.get("userId");
                    if (userIdObj != null) {
                        uid = userIdObj.toString();
                        experienciaMap.remove("userId");
                    }
                }
                if (uid == null) {
                    resultados.add(Map.of(
                        "error", "Falta el campo 'userId' en este objeto de experiencia. Debes incluirlo en cada objeto o como query param.",
                        "data", experienciaMap
                    ));
                    continue;
                }
                try {
                    ExperienciaDTO experienciaDTO = mapper.convertValue(experienciaMap, ExperienciaDTO.class);
                    Experiencia experiencia = usuarioService.agregarExperiencia(uid, experienciaDTO);
                    if (experiencia == null) {
                        resultados.add(Map.of("error", "Usuario no encontrado", "data", experienciaMap));
                    } else {
                        resultados.add(Map.of("experiencia", experiencia));
                    }
                } catch (Exception e) {
                    resultados.add(Map.of("error", "Formato de experiencia inválido", "data", experienciaMap));
                }
            }
            return ResponseEntity.ok(resultados);
        } else if (body instanceof java.util.Map) {
            java.util.Map<String, Object> experienciaMap = (java.util.Map<String, Object>) body;
            if (userId == null) {
                Object userIdObj = experienciaMap.get("userId");
                if (userIdObj != null) {
                    userId = userIdObj.toString();
                    experienciaMap.remove("userId");
                }
            }
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Falta el parámetro userId"));
            }
            try {
                ExperienciaDTO experienciaDTO = mapper.convertValue(experienciaMap, ExperienciaDTO.class);
                Experiencia experiencia = usuarioService.agregarExperiencia(userId, experienciaDTO);
                if (experiencia == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
                }
                return ResponseEntity.ok(Map.of("experiencia", experiencia));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Formato de experiencia inválido"));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato de body inválido"));
        }
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
            .map(usuario -> {
                Map<String, Object> response = new HashMap<>();
                Map<String, Object> usuarioMap = new HashMap<>();
                usuarioMap.put("userId", usuario.getUserId());
                usuarioMap.put("email", usuario.getEmail());
                usuarioMap.put("nombreCompleto", usuario.getNombreCompleto());
                usuarioMap.put("fotoUrl", usuario.getFotoUrl());
                usuarioMap.put("nacionalidad", usuario.getNacionalidad());
                usuarioMap.put("nacimiento", usuario.getNacimiento());
                usuarioMap.put("genero", usuario.getGenero());
                usuarioMap.put("estadoCivil", usuario.getEstadoCivil());
                usuarioMap.put("licencia", usuario.getLicencia());
                usuarioMap.put("contacto", usuario.getContacto());
                usuarioMap.put("descripcion", usuario.getDescripcion());
                usuarioMap.put("habilidades", usuario.getHabilidades());
                usuarioMap.put("cvAdjunto", usuario.getCvAdjunto());
                usuarioMap.put("resumenProfesional", usuario.getResumenProfesional());
                usuarioMap.put("estudios", usuario.getEstudios());
                usuarioMap.put("experiencias", usuario.getExperiencias());
                // Agregar rol_id y rol (nombre)
                if (usuario.getRol() != null) {
                    usuarioMap.put("rol_id", usuario.getRol().getId());
                    usuarioMap.put("rol", usuario.getRol().getNombre());
                } else {
                    usuarioMap.put("rol_id", null);
                    usuarioMap.put("rol", null);
                }
                response.put("usuario", usuarioMap);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("error", "Usuario no encontrado");
                return ResponseEntity.ok(errorMap);
            });
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
