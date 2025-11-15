package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "rol")
    private String rol;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
}
