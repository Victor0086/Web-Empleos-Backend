
package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Oferta;
import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.repository.OfertaRepository;
import com.empleos.web_empleos_backend.repository.PostulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostulacionService {
    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private OfertaRepository ofertaRepository; 

    @Autowired
    private ContratoService contratoService;


    public List<Postulacion> findAll() {
        return postulacionRepository.findAll();
    }

    public List<Postulacion> findByEmail(String email) {
        return postulacionRepository.findByUsuarioEmail(email);
    }

    public Optional<Postulacion> findById(Long ofertaId, String trabajadorId) {
        com.empleos.web_empleos_backend.model.PostulacionId id = new com.empleos.web_empleos_backend.model.PostulacionId(ofertaId, trabajadorId);
        return postulacionRepository.findById(id);
    }

    public Postulacion save(Postulacion postulacion) {
        return postulacionRepository.save(postulacion);
    }

    public void deleteById(Long ofertaId, String trabajadorId) {
        com.empleos.web_empleos_backend.model.PostulacionId id = new com.empleos.web_empleos_backend.model.PostulacionId(ofertaId, trabajadorId);
        postulacionRepository.deleteById(id);
    }

    public List<Postulacion> findByOfertaId(Long ofertaId) {
        return postulacionRepository.findByOfertaId(ofertaId);
    }

    @Transactional
    public Postulacion gestionarPostulacion(Long ofertaId, String trabajadorId, String nuevoEstado, String idEmpleadorActual) {
        
        com.empleos.web_empleos_backend.model.PostulacionId id = new com.empleos.web_empleos_backend.model.PostulacionId(ofertaId, trabajadorId);

        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));

        Oferta oferta = ofertaRepository.findById(postulacion.getOfertaId())
                .orElseThrow(() -> new RuntimeException("Oferta asociada no encontrada"));

        if (!oferta.getEmpleadorId().equals(idEmpleadorActual)) {
            throw new RuntimeException("No tienes permiso para gestionar esta postulación (no eres el autor de la oferta).");
        }

        if (!nuevoEstado.equals("ACEPTADA") && !nuevoEstado.equals("RECHAZADA")) {
             throw new RuntimeException("Estado inválido. Use 'ACEPTADA' o 'RECHAZADA'.");
        }
        
        postulacion.setEstado(nuevoEstado);
        Postulacion guardada = postulacionRepository.save(postulacion);

        if ("ACEPTADA".equals(nuevoEstado)) {
            contratoService.generarContratoInicial(guardada);
        }

        return guardada;
    }
}
