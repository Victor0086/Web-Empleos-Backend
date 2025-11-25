package com.empleos.web_empleos_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioDTO {
    private String titulo;
    private String institucion;
    private String tipo;
    private String estado;
    private String fechainicio;
    private String fechaFin;
    private String referencia;
    private Boolean certificado;
}
