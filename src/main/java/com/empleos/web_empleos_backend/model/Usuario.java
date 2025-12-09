package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", columnDefinition = "uniqueidentifier")
    private String userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nombre_completo", nullable = true)
    private String nombreCompleto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Role rol;

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

    @Column(name = "resumen_profesional")
    private String resumenProfesional;
    @Override
    public String toString() {
        return "Usuario{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", rol='" + rol + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fotoUrl='" + fotoUrl + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", nacimiento=" + nacimiento +
                ", genero='" + genero + '\'' +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", licencia='" + licencia + '\'' +
                ", contacto=" + contacto +
                ", estudiosCount=" + (estudios != null ? estudios.size() : 0) +
                ", experienciasCount=" + (experiencias != null ? experiencias.size() : 0) +
                ", descripcion='" + descripcion + '\'' +
                ", habilidades='" + habilidades + '\'' +
                ", cvAdjunto='" + cvAdjunto + '\'' +
                '}';
    }
}