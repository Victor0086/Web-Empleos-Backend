package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.service.PostulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {
    @Autowired
    private PostulacionService postulacionService;

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
