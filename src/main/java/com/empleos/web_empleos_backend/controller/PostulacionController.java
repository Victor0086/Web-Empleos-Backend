package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.service.PostulacionService;
import com.empleos.web_empleos_backend.service.UsuarioService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {
    @Autowired
    private PostulacionService postulacionService;


    // Endpoint seguro: obtiene el email del usuario autenticado vía Principal
    @GetMapping(params = "email")
    public ResponseEntity<?> getByEmail(@AuthenticationPrincipal Jwt jwt) {
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
        System.out.println("[DEBUG] Email extraído del JWT: " + email);
        var postulaciones = postulacionService.findByEmail(email);
        return ResponseEntity.ok().body(postulaciones);
    }


    @GetMapping("/{oferta_id}/{trabajador_id}")
    public ResponseEntity<Postulacion> getById(@PathVariable Long oferta_id, @PathVariable String trabajador_id) {
        return postulacionService.findById(oferta_id, trabajador_id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Postulacion create(@RequestBody Postulacion postulacion) {
        return postulacionService.save(postulacion);
    }

    @DeleteMapping("/{oferta_id}/{trabajador_id}")
    public ResponseEntity<Void> delete(@PathVariable Long oferta_id, @PathVariable String trabajador_id) {
        postulacionService.deleteById(oferta_id, trabajador_id);
        return ResponseEntity.noContent().build();
    }

    // Nuevo endpoint para crear postulaciones con archivo
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPostulacion(
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("rut") String rut,
            @RequestParam("telefono") String telefono,
            @RequestParam("email") String email,
            @RequestParam("experiencia") String experiencia,
            @RequestParam(value = "descripcionExperiencia", required = false) String descripcionExperiencia,
            @RequestParam(value = "motivacion", required = false) String motivacion,
            @RequestParam("oferta_id") Long ofertaId,
            @RequestParam(value = "curriculum", required = false) MultipartFile curriculum) {
        try {
            // Buscar usuario por email
            var usuarioOpt = usuarioService.buscarPorEmail(email);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "message", "No existe un usuario registrado con ese email"
                ));
            }
            var usuario = usuarioOpt.get();
            Postulacion postulacion = new Postulacion();
            postulacion.setOfertaId(ofertaId);
            postulacion.setTrabajadorId(usuario.getUserId());
            postulacion.setNombres(nombres);
            postulacion.setApellidos(apellidos);
            postulacion.setRut(rut);
            postulacion.setTelefono(telefono);
            postulacion.setEmail(email);
            postulacion.setExperiencia(experiencia);
            postulacion.setDescripcionExperiencia(descripcionExperiencia);
            postulacion.setMotivacion(motivacion);
            postulacion.setFecha_postulacion(new Date());
            postulacion.setEstado("ENVIADA");
            postulacion.setUsuario(usuario); //Aquí se asocia la postulación al usuario autenticado

            // Manejar archivo de currículum
            if (curriculum != null && !curriculum.isEmpty()) {
                String fileName = guardarArchivo(curriculum);
                postulacion.setCurriculumName(curriculum.getOriginalFilename());
                postulacion.setCurriculumPath(fileName);
            }

            Postulacion savedPostulacion = postulacionService.save(postulacion);
            return ResponseEntity.ok(Map.of(
                "message", "Postulación enviada exitosamente",
                "id", savedPostulacion.getOfertaId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "message", "Error al procesar postulación",
                "error", e.getMessage()
            ));
        }
    }

    private String guardarArchivo(MultipartFile archivo) throws IOException {
        String uploadDir = "uploads/curriculums/";
        String fileName = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    @GetMapping("/oferta/{oferta_id}")
    public List<Postulacion> getByOfertaId(@PathVariable Long oferta_id) {
        return postulacionService.findByOfertaId(oferta_id);
    }

    @PutMapping("/{oferta_id}/{trabajador_id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long oferta_id,
            @PathVariable String trabajador_id,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El estado es requerido"));
        }
        var postulacionOpt = postulacionService.findById(oferta_id, trabajador_id);
        if (postulacionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var postulacion = postulacionOpt.get();
        postulacion.setEstado(nuevoEstado);
        postulacionService.save(postulacion);
        return ResponseEntity.ok(Map.of("message", "Estado actualizado", "estado", nuevoEstado));
    }


}
