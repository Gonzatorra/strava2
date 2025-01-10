package com.BD.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "entrenamientos")
public class EntrenamientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "usuario", nullable = false)
    private String usuario;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "deporte", nullable = false)
    private String deporte;

    @Column(name = "distancia", nullable = false)
    private double distancia;

    @Column(name = "duracion", nullable = false)
    private double duracion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "hora_inicio", nullable = false)
    private float horaInicio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public float getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(float horaInicio) {
        this.horaInicio = horaInicio;
    }
}
