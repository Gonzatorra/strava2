package com.strava.dominio;

import java.util.*;
import java.io.Serializable;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Usuario implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    private String contrasena;
    private String nombre;
    private float peso;
    private float altura;
    private java.util.Date fNacimiento;
    private float frecCMax;
    private float frecCReposo;
    private String token;
    private ArrayList<Entrenamiento> entrenamientos;
    private HashMap<Integer, String> retos;
    private ArrayList<Integer> amigos;
    private String proveedor; //Inicio sesion proveedor



    public ArrayList<Integer> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<Integer> amigos) {
        this.amigos = amigos;
    }

    //constructores
    public Usuario() {
        super();
    }

    //sin valores opcionales
    public Usuario(int id, String username, String email, String contrasena,
                   String nombre, String token, String proveedor, ArrayList<Entrenamiento> entrenamientos,
                   HashMap<Integer, String> retos, ArrayList<Integer> amigos) {
        super();
        this.id = id;
        this.username = username;
        this.email = email;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.token = token;
        this.entrenamientos = entrenamientos;
        this.retos = retos;
        this.amigos = amigos;
        this.proveedor= proveedor;

    }

    public Usuario(int id, String username, String email, String contrasena,
                   String nombre, float peso, float altura, java.util.Date fNacimiento,
                   float frecCMax, float frecCReposo, String token, String proveedor, ArrayList<Entrenamiento> entrenamientos,
                   HashMap<Integer, String> retos, ArrayList<Integer> amigos) {
        super();
        this.id = id;
        this.username = username;
        this.email = email;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.peso = peso;
        this.altura = altura;
        this.fNacimiento = fNacimiento;
        this.frecCMax = frecCMax;
        this.frecCReposo = frecCReposo;
        this.token = token;
        this.entrenamientos = entrenamientos;
        this.retos = retos;
        this.amigos = amigos;
        this.proveedor= proveedor;

    }

    //getters - setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        return frecCMax;
    }

    public void setFecCMax(float fecCMax) {
        this.frecCMax = fecCMax;
    }

    public float getFecCReposo() {
        return frecCReposo;
    }

    public void setFecCReposo(float fecCReposo) {
        this.frecCReposo = fecCReposo;
    }

    public ArrayList<Entrenamiento> getEntrenamientos() {
        return entrenamientos;
    }

    public void setEntrenamientos(ArrayList<Entrenamiento> entrenamientos) {
        this.entrenamientos = entrenamientos;
    }

    public HashMap<Integer, String> getRetos() {
        return retos;
    }

    public void setRetos(HashMap<Integer, String> retos) {
        this.retos = retos;
    }


    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

}