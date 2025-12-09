package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Contrato;
import com.empleos.web_empleos_backend.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contratos")
@CrossOrigin(origins = {"http://localhost:4200", "https://web-empleos-front-gmeaa7c5eqctg4b2.eastus2-01.azurewebsites.net"})
public class ContratoController {
    @Autowired
    private ContratoService contratoService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    public List<Contrato> getAll() {
        return contratoService.findAll();
    }

    @GetMapping("/mis-contratos")
    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    public ResponseEntity<List<Contrato>> getMisContratos(@AuthenticationPrincipal Jwt jwt) {
        String idUsuario = jwt.getSubject();
        
        List<Contrato> contratos = contratoService.findByUsuario(idUsuario);
        
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> getById(@PathVariable Long id) {
        return (ResponseEntity<Contrato>) contratoService.findById(id)
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

    @PostMapping("/{id}/firmar")
@PreAuthorize("hasAuthority('SCOPE_access_as_user')")
public ResponseEntity<?> firmarContrato(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
    String idUsuario = jwt.getSubject();

    try {
        Contrato contrato = contratoService.firmarContrato(id, idUsuario);
        
        String rolFirma = "Desconocido";
        String idTrabajador = contrato.getPostulacion().getTrabajadorId();

        if (idUsuario.equals(idTrabajador)) {
            rolFirma = "Trabajador";
        } else {
            rolFirma = "Empleador / Notario";
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Contrato firmado exitosamente por: " + rolFirma);
        respuesta.put("contrato", contrato);
        
        return ResponseEntity.ok(respuesta);

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}
