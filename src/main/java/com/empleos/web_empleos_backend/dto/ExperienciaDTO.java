package com.empleos.web_empleos_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienciaDTO {
    private String empresa;
    private String puesto;
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;
}
