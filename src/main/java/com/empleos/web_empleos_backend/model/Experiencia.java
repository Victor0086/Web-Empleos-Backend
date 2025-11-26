package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Experiencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "usuario")
public class Experiencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String empresa;
    private String puesto;
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Usuario usuario;
}