package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Contrato;
import com.empleos.web_empleos_backend.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContratoService {
    @Autowired
    private ContratoRepository contratoRepository;

    public List<Contrato> findAll() {
        return contratoRepository.findAll();
    }

    public Optional<Contrato> findById(Long id) {
        return contratoRepository.findById(id);
    }

    public Contrato save(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    public void deleteById(Long id) {
        contratoRepository.deleteById(id);
    }
}
