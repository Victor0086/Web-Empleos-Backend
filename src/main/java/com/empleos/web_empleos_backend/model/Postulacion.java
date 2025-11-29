package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Postulaciones")
@IdClass(PostulacionId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Postulacion {
    @Id
    @Column(name = "oferta_id")
    private Long oferta_id;

    @Id
    @Column(name = "trabajador_id")
    private String trabajador_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_postulacion")
    private Date fecha_postulacion;
}
