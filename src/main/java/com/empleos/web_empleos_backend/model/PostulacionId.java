package com.empleos.web_empleos_backend.model;

import java.io.Serializable;
import java.util.Objects;

public class PostulacionId implements Serializable {
    private Long oferta_id;
    private String trabajador_id;

    public PostulacionId() {}

    public PostulacionId(Long oferta_id, String trabajador_id) {
        this.oferta_id = oferta_id;
        this.trabajador_id = trabajador_id;
    }

    public Long getOferta_id() { return oferta_id; }
    public void setOferta_id(Long oferta_id) { this.oferta_id = oferta_id; }
    public String getTrabajador_id() { return trabajador_id; }
    public void setTrabajador_id(String trabajador_id) { this.trabajador_id = trabajador_id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostulacionId that = (PostulacionId) o;
        return Objects.equals(oferta_id, that.oferta_id) && Objects.equals(trabajador_id, that.trabajador_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oferta_id, trabajador_id);
    }
}
