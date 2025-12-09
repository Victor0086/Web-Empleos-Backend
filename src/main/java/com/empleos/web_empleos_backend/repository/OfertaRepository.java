package com.empleos.web_empleos_backend.repository;

import com.empleos.web_empleos_backend.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    List<Oferta> findByEmpleadorId(String empleadorId);
}
