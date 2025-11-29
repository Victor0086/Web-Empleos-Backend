package com.empleos.web_empleos_backend.repository;

import com.empleos.web_empleos_backend.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
	List<Postulacion> findByUsuarioEmail(String email);
}
