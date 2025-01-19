package com.strava.dominio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Reto implements Serializable{
    //atributos
    private int id;
    private String deporte;
    private String usuarioCreador;
    private String nombre;
    private LocalDateTime fecIni;
    private LocalDateTime fecFin;
    private float objetivoDistancia;
    private float objetivoTiempo;
    private ArrayList<Integer> participantes;

    //constructores
    public Reto() {
        //constructor vacío
    }

    public Reto(int id, String deporte, String usuarioCreador, String nombre, LocalDateTime fecIni, LocalDateTime fecFin,
                float objetivoDistancia, float objetivoTiempo, ArrayList<Integer> participantes) {
        this.id = id;
        this.deporte = deporte;
        this.usuarioCreador = usuarioCreador;
        this.nombre = nombre;
        this.fecIni = fecIni;
        this.fecFin = fecFin;
        this.objetivoDistancia = objetivoDistancia;
        this.objetivoTiempo = objetivoTiempo;
        this.participantes = participantes;
    }

    //getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFecIni() {
        return fecIni;
    }

    public void setFecIni(LocalDateTime fecIni) {
        this.fecIni = fecIni;
    }

    public LocalDateTime getFecFin() {
        return fecFin;
    }

    public void setFecFin(LocalDateTime fecFin) {
        this.fecFin = fecFin;
    }

    public float getObjetivoDistancia() {
        return objetivoDistancia;
    }

    public void setObjetivoDistancia(float objetivoDistancia) {
        this.objetivoDistancia = objetivoDistancia;
    }

    public float getObjetivoTiempo() {
        return objetivoTiempo;
    }

    public void setObjetivoTiempo(float objetivoTiempo) {
        this.objetivoTiempo = objetivoTiempo;
    }

    public ArrayList<Integer> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Integer> participantes) {
        this.participantes = participantes;
    }


    public Reto actualizarReto( String nombre, LocalDateTime fecIni, LocalDateTime fecFin, float objetivoDistancia,
                                float objetivoTiempo, String usuarioCreador, String deporte, ArrayList<Integer> participantes) {
        System.out.println("Se actualiza el reto");

        this.setNombre(nombre);
        this.setFecIni(fecIni);
        this.setFecFin(fecFin);
        this.setObjetivoDistancia(objetivoDistancia);
        this.setObjetivoTiempo(objetivoTiempo);
        this.setUsuarioCreador(usuarioCreador);
        this.setDeporte(deporte);
        this.setParticipantes(participantes);
        return this;
    }


}