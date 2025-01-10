package com.BD.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reto_participantes")
public class RetoParticipantesEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "usuario_id", nullable = false)
    private int usuarioId;

    @Column(name = "reto_id", nullable = false)
    private int retoId;

    @Column(name = "estado", nullable = false)
    private String estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getRetoId() {
        return retoId;
    }

    public void setRetoId(int retoId) {
        this.retoId = retoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
