package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Postulaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Postulacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oferta_id;
    private String estado;
    private Date fecha_postulacion;
}
