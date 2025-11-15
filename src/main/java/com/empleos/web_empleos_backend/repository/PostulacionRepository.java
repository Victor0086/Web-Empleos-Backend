package com.empleos.web_empleos_backend.repository;

import com.empleos.web_empleos_backend.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {}
