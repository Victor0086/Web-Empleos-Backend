package com.empleos.web_empleos_backend.model;

import java.io.Serializable;
import java.util.Objects;

public class PostulacionId implements Serializable {
    private Long ofertaId;
    private String trabajadorId;

    public PostulacionId() {}

    public PostulacionId(Long ofertaId, String trabajadorId) {
        this.ofertaId = ofertaId;
        this.trabajadorId = trabajadorId;
    }

    public Long getOfertaId() { return ofertaId; }
    public void setOfertaId(Long ofertaId) { this.ofertaId = ofertaId; }
    public String getTrabajadorId() { return trabajadorId; }
    public void setTrabajadorId(String trabajadorId) { this.trabajadorId = trabajadorId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostulacionId that = (PostulacionId) o;
        return Objects.equals(ofertaId, that.ofertaId) && Objects.equals(trabajadorId, that.trabajadorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ofertaId, trabajadorId);
    }
}
