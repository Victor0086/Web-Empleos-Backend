package com.empleos.web_empleos_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilDTO {
    private String userId;
    private String email;
    private String nombreCompleto;
    private String rol;
    private Date fechaCreacion;
    private String fotoUrl;
    private String nacionalidad;
    private Date nacimiento;
    private String genero;
    private String estadoCivil;
    private String licencia;
    private ContactoDTO contacto;
    private List<EstudioDTO> estudios;
    private List<ExperienciaDTO> experiencias;
    private String descripcion;
    private String habilidades;
    private String cvAdjunto;
}