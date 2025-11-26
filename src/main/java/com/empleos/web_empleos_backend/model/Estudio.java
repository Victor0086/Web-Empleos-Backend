package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Estudios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String institucion;
    private String tipo;
    private String estado;
    @Column(name = "fecha_inicio")
    private String fechainicio;

    @Column(name = "fecha_fin")
    private String fechaFin;
    private String referencia;
    private Boolean certificado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    // Getter y Setter para usuario
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}