package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "experiencias")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", columnDefinition = "uniqueidentifier")
    private String userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nombre_completo", nullable = true)
    private String nombreCompleto;

    @Column(name = "rol")
    private String rol;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "nacionalidad")
    private String nacionalidad;

    @Column(name = "nacimiento")
    private Date nacimiento;

    @Column(name = "genero")
    private String genero;

    @Column(name = "estado_civil")
    private String estadoCivil;

    @Column(name = "licencia")
    private String licencia;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "contacto_email"))
    private Contacto contacto;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private List<Estudio> estudios;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private List<Experiencia> experiencias;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "habilidades")
    private String habilidades;

    @Column(name = "cv_adjunto")
    private String cvAdjunto;
}