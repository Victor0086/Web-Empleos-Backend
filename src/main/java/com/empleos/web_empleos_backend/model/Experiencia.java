package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Experiencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experiencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String empresa;
    private String puesto;
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;
}