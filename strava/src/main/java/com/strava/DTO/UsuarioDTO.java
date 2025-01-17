package com.strava.DTO;

import java.io.Serializable;
import java.util.*;


public class UsuarioDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    //atributos
    private int id;
    private String username;
    private String email;
    private String contrasena;
    private String nombre;
    private float peso;
    private float altura;
    private java.util.Date fNacimiento;
    private float fecCMax;
    private float fecCReposo;
    private String token;
    private ArrayList<EntrenamientoDTO> entrenamientos;
    private HashMap<RetoDTO, String> retos;
    private ArrayList<Integer> amigos;
    private String proveedor; // Nuevo atributo


    public UsuarioDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UsuarioDTO(String username, int id, String email, String contrasena, String nombre, float altura, float peso, Date fNacimiento, String token) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.altura = altura;
        this.peso = peso;
        this.fNacimiento = fNacimiento;
        this.token = token;
    }

    public ArrayList<Integer> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<Integer> amigosDT) {
        this.amigos = amigosDT;
    }

    //getters - setters
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

    public java.util.Date getfNacimiento() {
        return fNacimiento;
    }

    public void setfNacimiento(java.util.Date fNacimiento) {
        this.fNacimiento = fNacimiento;
    }

    public float getFecCMax() {
        return fecCMax;
    }

    public void setFecCMax(float fecCMax) {
        this.fecCMax = fecCMax;
    }

    public float getFecCReposo() {
        return fecCReposo;
    }

    public void setFecCReposo(float fecCReposo) {
        this.fecCReposo = fecCReposo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<EntrenamientoDTO> getEntrenamientos() {
        return entrenamientos;
    }

    public void setEntrenamientos(ArrayList<EntrenamientoDTO> entrenamientos) {
        this.entrenamientos = entrenamientos;
    }

    public HashMap<RetoDTO, String> getRetos() {
        return retos;
    }

    public void setRetos(HashMap<RetoDTO, String> retos) {
        this.retos = retos;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

}