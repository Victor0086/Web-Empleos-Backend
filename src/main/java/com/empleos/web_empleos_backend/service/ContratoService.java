package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Contrato;
import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.repository.ContratoRepository;
import com.empleos.web_empleos_backend.repository.OfertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    public List<Contrato> findAll() {
        return contratoRepository.findAll();
    }

    public Optional<Contrato> findById(Long id) {
        return contratoRepository.findById(id);
    }

    public List<Contrato> findByUsuario(String userId) {
        return contratoRepository.findByUsuario(userId);
    }

    public Contrato save(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    public void deleteById(Long id) {
        contratoRepository.deleteById(id);
    }

    public Contrato generarContratoInicial(Postulacion postulacion) {
        if (contratoRepository.findByPostulacion(postulacion).isPresent()) {
            throw new RuntimeException("Ya existe un contrato generado para esta postulación.");
        }

        Contrato nuevoContrato = new Contrato(postulacion);
        return contratoRepository.save(nuevoContrato);
    }

    @Transactional
    public Contrato firmarContrato(Long idContrato, String idUsuario) {
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado."));

        String idTrabajador = contrato.getPostulacion().getTrabajadorId();
        
        String idEmpleador = ofertaRepository.findById(contrato.getPostulacion().getOfertaId())
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada")).getEmpleadorId();

        boolean esTrabajador = idUsuario.equals(idTrabajador);
        boolean esEmpleador = idUsuario.equals(idEmpleador);

        if (esTrabajador) {
            if (contrato.getFirmaTrabajador() != null) {
                 throw new RuntimeException("Error: Ya has firmado este contrato anteriormente.");
            }
            contrato.setFirmaTrabajador(LocalDateTime.now());
            
        } else if (esEmpleador) {
            if (contrato.getFirmaEmpleador() != null) {
                 throw new RuntimeException("Error: Ya has firmado este contrato anteriormente.");
            }
            contrato.setFirmaEmpleador(LocalDateTime.now());
            
        } else {
            contrato.setFirmaNotario(LocalDateTime.now());
            contrato.setIdNotario(idUsuario);
        }

        if (contrato.getFirmaTrabajador() != null && contrato.getFirmaEmpleador() != null) {
            contrato.setEstado("ACTIVO");
        }

        return contratoRepository.save(contrato);
    }
}