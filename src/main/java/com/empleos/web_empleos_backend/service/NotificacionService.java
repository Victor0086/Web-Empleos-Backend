package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Notificacion;
import com.empleos.web_empleos_backend.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;

    public Notificacion crearNotificacion(String destinatario, String mensaje) {
        Notificacion notificacion = new Notificacion(destinatario, mensaje);
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerNotificaciones(String destinatario) {
        return notificacionRepository.findAll()
            .stream()
            .filter(n -> n.getDestinatario().equals(destinatario))
            .toList();
    }
}
