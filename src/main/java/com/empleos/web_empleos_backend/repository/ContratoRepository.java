package com.empleos.web_empleos_backend.repository;

import com.empleos.web_empleos_backend.model.Contrato;
import com.empleos.web_empleos_backend.model.Postulacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    @Query("SELECT c FROM Contrato c WHERE c.postulacion.trabajadorId = :userId OR c.postulacion.ofertaId IN (SELECT o.oferta_id FROM Oferta o WHERE o.empleadorId = :userId) OR c.postulacion.email = :email")
    List<Contrato> findContratosPorTrabajadorOEmpleadorOEmail(@Param("userId") String userId, @Param("email") String email);
    Optional<Contrato> findByPostulacion(Postulacion postulacion);

    @Query("SELECT c FROM Contrato c " +
           "WHERE c.postulacion.trabajadorId = :userId " +
           "OR c.postulacion.ofertaId IN (SELECT o.id FROM Oferta o WHERE o.empleadorId = :userId)")
    List<Contrato> findByUsuario(@Param("userId") String userId);
}