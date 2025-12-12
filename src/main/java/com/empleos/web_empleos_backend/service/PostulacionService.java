
package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.repository.PostulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostulacionService {
    @Autowired
    private PostulacionRepository postulacionRepository;


    public List<Postulacion> findAll() {
        return postulacionRepository.findAll();
    }

    public List<Postulacion> findByEmail(String email) {
        System.out.println("[DEBUG] Email recibido para búsqueda: " + email);
        
        // Primero veremos todas las postulaciones para depurar
        List<Postulacion> todasLasPostulaciones = postulacionRepository.findAll();
        System.out.println("[DEBUG] Total de postulaciones en BD: " + todasLasPostulaciones.size());
        
        for (Postulacion p : todasLasPostulaciones) {
            System.out.println("[DEBUG] Postulación - OfertaId: " + p.getOfertaId() + 
                             ", TrabajadorId: " + p.getTrabajadorId() + 
                             ", Email: " + p.getEmail() + 
                             ", Usuario: " + (p.getUsuario() != null ? p.getUsuario().getEmail() : "NULL"));
        }
        
        List<Postulacion> result = postulacionRepository.findByUsuarioEmailIgnoreCase(email);
        System.out.println("[DEBUG] Postulaciones encontradas por usuario.email: " + result.size());
        return result;
    }
    

    public Optional<Postulacion> findById(Long ofertaId, String trabajadorId) {
        com.empleos.web_empleos_backend.model.PostulacionId id = new com.empleos.web_empleos_backend.model.PostulacionId(ofertaId, trabajadorId);
        return postulacionRepository.findById(id);
    }

    public Postulacion save(Postulacion postulacion) {
        System.out.println("[DEBUG] Guardando postulación - OfertaId: " + postulacion.getOfertaId() + 
                         ", TrabajadorId: " + postulacion.getTrabajadorId() + 
                         ", Email: " + postulacion.getEmail() + 
                         ", Usuario asignado: " + (postulacion.getUsuario() != null ? postulacion.getUsuario().getEmail() : "NULL"));
        
        Postulacion saved = postulacionRepository.save(postulacion);
        
        System.out.println("[DEBUG] Postulación guardada - ID: " + saved.getOfertaId() + "/" + saved.getTrabajadorId());
        return saved;
    }

    public void deleteById(Long ofertaId, String trabajadorId) {
        com.empleos.web_empleos_backend.model.PostulacionId id = new com.empleos.web_empleos_backend.model.PostulacionId(ofertaId, trabajadorId);
        postulacionRepository.deleteById(id);
    }

    public List<Postulacion> findByOfertaId(Long ofertaId) {
        return postulacionRepository.findByOfertaId(ofertaId);
    }
}
