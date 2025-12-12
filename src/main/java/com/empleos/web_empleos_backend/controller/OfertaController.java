package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Oferta;
import com.empleos.web_empleos_backend.service.OfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {
    @Autowired
    private OfertaService ofertaService;

    @GetMapping
    public List<Oferta> getAll() {
        return ofertaService.findAll();
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<Oferta> actualizarEstado(@PathVariable Long id, @RequestBody(required = true)  java.util.Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        Oferta ofertaActualizada = ofertaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(ofertaActualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oferta> getById(@PathVariable Long id) {
        return ofertaService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Oferta create(@RequestBody Oferta oferta) {
        return ofertaService.save(oferta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ofertaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/empleador/{empleadorId}")
    public List<Oferta> getByEmpleadorId(@PathVariable String empleadorId) {
        return ofertaService.findByEmpleadorId(empleadorId);
    }
}
