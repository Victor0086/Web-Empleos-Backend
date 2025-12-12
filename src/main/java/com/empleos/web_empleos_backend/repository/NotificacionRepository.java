package com.empleos.web_empleos_backend.repository;

import com.empleos.web_empleos_backend.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}
