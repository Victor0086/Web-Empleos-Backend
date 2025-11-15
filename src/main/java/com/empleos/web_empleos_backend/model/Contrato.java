package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Contratos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contrato_id;
    private Long oferta_id;
    private String trabajador_id;
    private String notario_id;
    private String estado;
    private Date fecha_firma_trabajador;
    private Date fecha_firma_empleador;
    private Date fecha_firma_notario;
    private Date fecha_creacion;
}
