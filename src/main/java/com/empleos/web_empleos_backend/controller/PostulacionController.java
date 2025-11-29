package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.service.PostulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {
    @Autowired
    private PostulacionService postulacionService;

    @Autowired
    private com.empleos.web_empleos_backend.service.JwtService jwtService;

    // Endpoint seguro: obtiene el email del usuario autenticado vía JWT
    @GetMapping(params = "email")
    public ResponseEntity<?> getByEmail(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        // Buscar postulaciones por email
        var postulaciones = postulacionService.findByEmail(email);
        return ResponseEntity.ok().body(postulaciones);
    }

    @GetMapping
    public List<Postulacion> getAll() {
        return postulacionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Postulacion> getById(@PathVariable Long id) {
        return postulacionService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Postulacion create(@RequestBody Postulacion postulacion) {
        return postulacionService.save(postulacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postulacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
