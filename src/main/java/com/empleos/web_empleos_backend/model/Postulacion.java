package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Postulaciones")
@IdClass(PostulacionId.class) 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Postulacion {
    @Id
    @Column(name = "oferta_id")
    private Long ofertaId;

    @Id
    @Column(name = "trabajador_id")
    private String trabajadorId;

    @Column(name = "nombres", nullable = false)
    private String nombres;
    
    @Column(name = "apellidos", nullable = false)
    private String apellidos;
    
    @Column(name = "rut", nullable = false)
    private String rut;
    
    @Column(name = "telefono", nullable = false)
    private String telefono;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "experiencia", nullable = false)
    private String experiencia;
    
    @Column(name = "descripcion_experiencia", length = 2000)
    private String descripcionExperiencia;
    
    @Column(name = "motivacion", length = 2000)
    private String motivacion;
    
    @Column(name = "curriculum_path")
    private String curriculumPath;
    
    @Column(name = "curriculum_name")
    private String curriculumName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "estado", nullable = false)
    private String estado = "pendiente";

    @Column(name = "fecha_postulacion", nullable = false)
    private Date fecha_postulacion = new Date();
}