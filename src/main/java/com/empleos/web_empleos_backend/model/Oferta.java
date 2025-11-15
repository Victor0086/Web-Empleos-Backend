package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Ofertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oferta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oferta_id;
    private String empleador_id;
    private String titulo;
    private String descripcion;
    private String contrato_type;
    private String location;
    private String estado;
    private Date fecha_creacion;
    private Date fecha_modificacion;
}
