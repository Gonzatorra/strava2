package com.strava.DTO;


import com.strava.dominio.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RetoDTO implements Serializable{
    //atributos
    private int id;
    private String deporte;
    private UsuarioDTO usuarioCreador;
    private String nombre;
    private LocalDateTime fecIni;
    private LocalDateTime fecFin;
    private float objetivoDistancia;
    private float objetivoTiempo;
    private ArrayList<Integer> participantes;

    //constructor
    public RetoDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    //getters
    public int getId() {
        return id;
    }

    public String getDeporte() {
        return deporte;
    }

    public UsuarioDTO getUsuarioCreador() {
        return usuarioCreador;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFecIni() {
        return fecIni;
    }

    public LocalDateTime getFecFin() {
        return fecFin;
    }

    public float getObjetivoDistancia() {
        return objetivoDistancia;
    }

    public float getObjetivoTiempo() {
        return objetivoTiempo;
    }

    public ArrayList<Integer> getParticipantes() {
        return participantes;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public void setUsuarioCreador(UsuarioDTO usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecIni(LocalDateTime fecIni) {
        this.fecIni = fecIni;
    }

    public void setFecFin(LocalDateTime fecFin) {
        this.fecFin = fecFin;
    }

    public void setObjetivoDistancia(float objetivoDistancia) {
        this.objetivoDistancia = objetivoDistancia;
    }

    public void setObjetivoTiempo(float objetivoTiempo) {
        this.objetivoTiempo = objetivoTiempo;
    }

    public void setParticipantes(ArrayList<Integer> participantes) {
        this.participantes = participantes;
    }


}
