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
import java.util.Optional;

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
        
        // Extraer email del JWT
        String email = null;
        Object preferred = jwt.getClaim("preferred_username");
        if (preferred != null) {
            email = preferred.toString();
        } else {
            Object emailsObj = jwt.getClaim("emails");
            if (emailsObj instanceof java.util.List<?> emailsList && !emailsList.isEmpty()) {
                email = emailsList.get(0).toString();
            }
        }
        
        System.out.println("[DEBUG] idUsuario recibido en /mis-contratos: " + idUsuario);
        System.out.println("[DEBUG] email extraído del JWT: " + email);
        
        // Obtener contratos donde soy trabajador O empleador
        List<Contrato> contratos = contratoService.findContratosPorTrabajadorOEmpleadorOEmail(idUsuario, email);
        
        System.out.println("[DEBUG] contratos encontrados: " + contratos.size());
        
        // Agregar información de debug para cada contrato
        for (Contrato contrato : contratos) {
            String trabajadorId = contrato.getPostulacion().getTrabajadorId();
            String emailTrabajador = contrato.getPostulacion().getEmail();
            System.out.println("[DEBUG] Contrato ID: " + contrato.getId() + 
                             " - Trabajador: " + trabajadorId + 
                             " - Email: " + emailTrabajador +
                             " - Estado: " + contrato.getEstado());
        }
        
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/mis-contratos-empleador")
    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    public ResponseEntity<List<Contrato>> getMisContratosEmpleador(@AuthenticationPrincipal Jwt jwt) {
        // Redirigir al endpoint principal para compatibilidad
        return getMisContratos(jwt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> getById(@PathVariable Long id) {
        return (ResponseEntity<Contrato>) contratoService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    public ResponseEntity<?> rechazarContrato(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        String idUsuario = jwt.getSubject();
        String email = null;
        Object preferred = jwt.getClaim("preferred_username");
        if (preferred != null) {
            email = preferred.toString();
        } else {
            Object emailsObj = jwt.getClaim("emails");
            if (emailsObj instanceof java.util.List<?> emailsList && !emailsList.isEmpty()) {
                email = emailsList.get(0).toString();
            }
        }
        try {
            contratoService.rechazarContrato(id, idUsuario, email);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contrato rechazado correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(400).body(error);
        }
    }

    @Autowired
    private com.empleos.web_empleos_backend.service.PostulacionService postulacionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        try {
            // Permitir snake_case y camelCase
            Object ofertaIdObj = body.get("ofertaId") != null ? body.get("ofertaId") : body.get("oferta_id");
            Object trabajadorIdObj = body.get("trabajadorId") != null ? body.get("trabajadorId") : body.get("trabajador_id");
            if (ofertaIdObj == null || trabajadorIdObj == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Faltan ofertaId o trabajadorId en la solicitud"));
            }
            Long ofertaId = Long.valueOf(ofertaIdObj.toString());
            String trabajadorId = trabajadorIdObj.toString();

            Optional<com.empleos.web_empleos_backend.model.Postulacion> postulacionOpt = postulacionService.findById(ofertaId, trabajadorId);
            if (postulacionOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Postulación no encontrada"));
            }
            com.empleos.web_empleos_backend.model.Postulacion postulacion = postulacionOpt.get();

            // Usar el servicio existente para generar el contrato inicial
            Contrato contrato = contratoService.generarContratoInicial(postulacion);
            
            return ResponseEntity.ok(contrato);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error al crear contrato", "error", e.getMessage()));
        }
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
    
    // Extraer email del JWT
    String email = null;
    Object preferred = jwt.getClaim("preferred_username");
    if (preferred != null) {
        email = preferred.toString();
    } else {
        Object emailsObj = jwt.getClaim("emails");
        if (emailsObj instanceof java.util.List<?> emailsList && !emailsList.isEmpty()) {
            email = emailsList.get(0).toString();
        }
    }

    try {
        Contrato contrato = contratoService.firmarContrato(id, idUsuario, email);
        
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
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
}