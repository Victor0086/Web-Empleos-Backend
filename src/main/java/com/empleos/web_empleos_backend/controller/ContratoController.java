package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Contrato;
import com.empleos.web_empleos_backend.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {
    @Autowired
    private ContratoService contratoService;

    @GetMapping
    public List<Contrato> getAll() {
        return contratoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> getById(@PathVariable Long id) {
        return contratoService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Contrato create(@RequestBody Contrato contrato) {
        return contratoService.save(contrato);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contratoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
