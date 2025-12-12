package com.empleos.web_empleos_backend.controller;

import com.empleos.web_empleos_backend.model.Notificacion;
import com.empleos.web_empleos_backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = {"http://localhost:4200", "https://web-empleos-front-gmeaa7c5eqctg4b2.eastus2-01.azurewebsites.net"})
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<Notificacion> crear(@RequestBody Map<String, String> body) {
        String destinatario = body.get("destinatario");
        String mensaje = body.get("mensaje");
        if (destinatario == null || mensaje == null) {
            return ResponseEntity.badRequest().build();
        }
        Notificacion notificacion = notificacionService.crearNotificacion(destinatario, mensaje);
        return ResponseEntity.ok(notificacion);
    }

    @GetMapping("/{destinatario}")
    public ResponseEntity<List<Notificacion>> obtener(@PathVariable String destinatario) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificaciones(destinatario);
        return ResponseEntity.ok(notificaciones);
    }
}
