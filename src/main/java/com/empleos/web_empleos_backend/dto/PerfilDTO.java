package com.empleos.web_empleos_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilDTO {
    private String descripcion;
    private String habilidades;
}
