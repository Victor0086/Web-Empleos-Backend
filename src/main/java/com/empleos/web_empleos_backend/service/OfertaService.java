package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Oferta;
import com.empleos.web_empleos_backend.repository.OfertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OfertaService {
    @Autowired
    private OfertaRepository ofertaRepository;

    public List<Oferta> findAll() {
        return ofertaRepository.findAll();
    }

    public Optional<Oferta> findById(Long id) {
        return ofertaRepository.findById(id);
    }
    public Oferta actualizarEstado(Long id, String nuevoEstado) {
        Oferta oferta = ofertaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada"));
        oferta.setEstado(nuevoEstado);
        return ofertaRepository.save(oferta);
    }

    public Oferta save(Oferta oferta) {
        // Validación de estado
        if (oferta.getEstado() == null ||
            !(oferta.getEstado().equals("ABIERTA") || oferta.getEstado().equals("CERRADA"))) {
            throw new IllegalArgumentException("El estado de la oferta solo puede ser 'ABIERTA' o 'CERRADA'");
        }
        // Validación de contrato_type
        if (oferta.getContrato_type() == null ||
            !(oferta.getContrato_type().equals("INDEFINIDO") || oferta.getContrato_type().equals("TEMPORAL"))) {
            throw new IllegalArgumentException("El tipo de contrato solo puede ser 'INDEFINIDO' o 'TEMPORAL'");
        }
        return ofertaRepository.save(oferta);
    }

    public void deleteById(Long id) {
        ofertaRepository.deleteById(id);
    }

    public List<Oferta> findByEmpleadorId(String empleadorId) {
        return ofertaRepository.findByEmpleadorId(empleadorId);
    }
}
