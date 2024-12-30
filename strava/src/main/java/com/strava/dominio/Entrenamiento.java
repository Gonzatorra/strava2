package com.strava.dominio;

import java.io.Serializable;
import java.time.LocalDate;

import com.strava.DTO.*;

public class Entrenamiento implements Serializable{
    //atributos

    private int id;
    private String username;
    private String titulo;
    private String deporte;
    private double distancia;
    private LocalDate fecIni;
    private float horaIni;
    private double duracion;

    //constructores
    public Entrenamiento(int id, String username, String titulo, String deporte, double distancia,
                         LocalDate fecIni, float horaIni, double duracion) {
        this.id = id;
        this.username = username;
        this.titulo = titulo;
        this.deporte = deporte;
        this.distancia = distancia;
        this.fecIni = fecIni;
        this.horaIni = horaIni;
        this.duracion = duracion;
    }



    //getters - setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return username;
    }

    public void setUsuario(String username) {
        this.username = username;
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

    public LocalDate getFecIni() {
        return fecIni;
    }

    public void setFecIni(LocalDate fecIni) {
        this.fecIni = fecIni;
    }

    public float getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(float horaIni) {
        this.horaIni = horaIni;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    //metodos
    public void actualizarEntreno(Entrenamiento entreno, double distancia, double fechaIni,
                                  float horaInicio, double duracion) {
        System.out.println("Se actualiza el entrenamiento");
        entreno.setDistancia((float) distancia);
        entreno.setFecIni(LocalDate.ofEpochDay((long) fechaIni));
        entreno.setHoraIni(horaInicio);
        entreno.setDuracion(duracion);
    }

    public void eliminarEntreno(Entrenamiento entreno) {
        System.out.println("Se elimina el entrenamiento");
        //si estuviera en una lista o colección, eliminaría el objeto aquí.
    }
}