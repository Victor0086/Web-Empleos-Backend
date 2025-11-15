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

    public Optional<Postulacion> findById(Long id) {
        return postulacionRepository.findById(id);
    }

    public Postulacion save(Postulacion postulacion) {
        return postulacionRepository.save(postulacion);
    }

    public void deleteById(Long id) {
        postulacionRepository.deleteById(id);
    }
}
