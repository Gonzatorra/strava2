package com.strava.fachada;

import com.google.server.UsuarioRepository;
import com.strava.DTO.*;
import com.strava.dominio.*;
import com.strava.servicios.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IRemoteFacade extends Remote {
    //usuario
    UsuarioDTO registrarUsuario(String username, String contrasena, String email, String nombre, String proveedor) throws RemoteException;
    UsuarioDTO login(String username, String contrasena, String plataforma) throws IOException, RemoteException;
    //UsuarioDTO loginConProveedor(String username, String password, String proveedor) throws IOException;
    void logout(String token) throws RemoteException;
    void eliminarUsuario(int userId) throws RemoteException;
    void actualizarUsuario(UsuarioDTO usuarioDTO) throws RemoteException;
    ArrayList<Integer> getAmigos(UsuarioDTO usuario) throws RemoteException;
    UsuarioService getUsuarioService() throws RemoteException;
    HashMap<Integer, UsuarioDTO> getUsuarios() throws RemoteException;
    List<String> getUsersActivos() throws RemoteException;

    //entrenamiento
    EntrenamientoDTO crearEntreno(UsuarioDTO usuario, String titulo, String deporte, double distancia, LocalDate fechaIni, float horaInicio, double duracion) throws java.rmi.RemoteException;
    void actualizarEntreno(EntrenamientoDTO entrenamiento, UsuarioDTO usu, String titulo, String deporte, double distancia, double duracion) throws RemoteException;
    void eliminarEntreno(int index, EntrenamientoDTO entrenamiento) throws java.rmi.RemoteException;
    void visualizarEntreno(EntrenamientoDTO entrenamiento) throws java.rmi.RemoteException;

    //reto
    RetoDTO crearReto(String nombre, LocalDateTime fecIni, LocalDateTime fecFin, float objetivoDistancia, float objetivoTiempo, String deporte, UsuarioDTO usuarioCreador, List<UsuarioDTO> participantes) throws RemoteException;
    void aceptarReto(UsuarioDTO usuario, RetoDTO reto) throws RemoteException;
    HashMap<Integer,RetoDTO> visualizarReto() throws RemoteException;
    void actualizarReto(RetoDTO reto, String nombre, LocalDateTime fechaIni, LocalDateTime fechaFin, float distancia, float tiempo, String usuarioCreador, String deporte, ArrayList<Integer> participantes) throws RemoteException;
    void eliminarReto(UsuarioDTO usuario, RetoDTO reto) throws RemoteException;
    List<Integer> obtenerClasificacion(RetoDTO reto) throws RemoteException;
    void cambiarEstado(UsuarioDTO usuario, RetoDTO reto, String estado) throws RemoteException;

}