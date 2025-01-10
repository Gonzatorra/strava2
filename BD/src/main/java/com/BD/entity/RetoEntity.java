package com.BD.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "retos")
public class RetoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "deporte", nullable = false)
    private String deporte;

    @Column(name = "creador", nullable = false)
    private String usuarioCreador;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fecIni;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fecFin;

    @Column(name = "objetivo_distancia", nullable = false)
    private float objetivoDistancia;

    @Column(name = "objetivo_tiempo", nullable = false)
    private float objetivoTiempo;

    @ElementCollection
    @CollectionTable(name = "reto_participantes", joinColumns = @JoinColumn(name = "reto_id"))
    @Column(name = "participante_id")
    private List<Integer> participantes = new ArrayList<>();

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

    public List<Integer> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Integer> participantes) {
        this.participantes = participantes;
    }
    
}
