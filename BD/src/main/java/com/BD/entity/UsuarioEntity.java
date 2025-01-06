package com.BD.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "peso")
    private float peso;

    @Column(name = "altura")
    private float altura;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento")
    private Date fNacimiento;

    @Column(name = "frecuencia_cardiaca_maxima")
    private float frecCMax;

    @Column(name = "frecuencia_cardiaca_reposo")
    private float frecCReposo;

    @Column(name = "token")
    private String token;

    @Column(name = "proveedor")
    private String proveedor;

    @OneToMany(mappedBy = "usuarioCreador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RetoEntity> retosCreados = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EntrenamientoEntity> entrenamientos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "usuario_amigos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "amigo_id")
    private List<Integer> amigos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "usuario_retos", joinColumns = @JoinColumn(name = "usuario_id"))
    @MapKeyJoinColumn(name = "reto_id")
    @Column(name = "estado")
    private HashMap<RetoEntity, String> retos = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public Date getfNacimiento() {
        return fNacimiento;
    }

    public void setfNacimiento(Date fNacimiento) {
        this.fNacimiento = fNacimiento;
    }

    public float getFrecCMax() {
        return frecCMax;
    }

    public void setFrecCMax(float frecCMax) {
        this.frecCMax = frecCMax;
    }

    public float getFrecCReposo() {
        return frecCReposo;
    }

    public void setFrecCReposo(float frecCReposo) {
        this.frecCReposo = frecCReposo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public List<RetoEntity> getRetosCreados() {
        return retosCreados;
    }

    public void setRetosCreados(List<RetoEntity> retosCreados) {
        this.retosCreados = retosCreados;
    }

    public List<EntrenamientoEntity> getEntrenamientos() {
        return entrenamientos;
    }

    public void setEntrenamientos(List<EntrenamientoEntity> entrenamientos) {
        this.entrenamientos = entrenamientos;
    }

    public List<Integer> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<Integer> amigos) {
        this.amigos = amigos;
    }

    public HashMap<RetoEntity, String> getRetos() {
        return retos;
    }

    public void setRetos(HashMap<RetoEntity, String> retos) {
        this.retos = retos;
    }
}
