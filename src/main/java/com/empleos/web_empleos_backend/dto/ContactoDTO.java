package com.empleos.web_empleos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactoDTO {
    private String celular;
    private String telefono;
    private String email;
    private String direccion;
}
