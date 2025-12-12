package com.empleos.web_empleos_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrato")
    private Long id;

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "oferta_id", referencedColumnName = "oferta_id", nullable = false),
        @JoinColumn(name = "trabajador_id", referencedColumnName = "trabajador_id", nullable = false)
    })
    private Postulacion postulacion;

    @Column(name = "id_notario")
    private String idNotario;

    @Column(nullable = false)
    private String estado; // 'PENDIENTE_FIRMAS', 'ACTIVO', 'FINALIZADO', 'CANCELADO'

    // Fechas de firma (nulas al principio)
    @Column(name = "firma_trabajador")
    private LocalDateTime firmaTrabajador;

    @Column(name = "firma_empleador")
    private LocalDateTime firmaEmpleador;

    @Column(name = "firma_notario")
    private LocalDateTime firmaNotario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE_FIRMAS";
        }
    }

    public Contrato() {
    }

    public Contrato(Postulacion postulacion) {
        this.postulacion = postulacion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
    }

    public String getIdNotario() {
        return idNotario;
    }

    public void setIdNotario(String idNotario) {
        this.idNotario = idNotario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFirmaTrabajador() {
        return firmaTrabajador;
    }

    public void setFirmaTrabajador(LocalDateTime firmaTrabajador) {
        this.firmaTrabajador = firmaTrabajador;
    }

    public LocalDateTime getFirmaEmpleador() {
        return firmaEmpleador;
    }

    public void setFirmaEmpleador(LocalDateTime firmaEmpleador) {
        this.firmaEmpleador = firmaEmpleador;
    }

    public LocalDateTime getFirmaNotario() {
        return firmaNotario;
    }

    public void setFirmaNotario(LocalDateTime firmaNotario) {
        this.firmaNotario = firmaNotario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}