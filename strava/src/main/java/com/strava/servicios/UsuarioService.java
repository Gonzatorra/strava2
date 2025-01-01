package com.strava.servicios;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.strava.DTO.*;
import com.strava.assembler.*;
import com.strava.dominio.*;

public class UsuarioService {

    public static HashMap<Integer, UsuarioDTO> usuarios = new HashMap<>();
    public static int idCounter = 1;  //para signar IDs unicos
    public static UsuarioService instancia;



    public UsuarioService() {
        usuarios = new HashMap<>();
        idCounter = 1;
        System.out.println("UsuarioService inicializado.");
    }

    public UsuarioDTO registrar(String username, String contrasena, String email, String nombre, String proveedor) {
        int nuevoId = idCounter++;
        Usuario usuario = new Usuario(nuevoId, username, email, contrasena, nombre, null, proveedor, new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        usuarios.put(UsuarioAssembler.toDTO(usuario).getId(), UsuarioAssembler.toDTO(usuario));
        System.out.println("Usuario registrado: " + username);
        return UsuarioAssembler.toDTO(usuario);
    }

    public UsuarioDTO login(String username, String contrasena) {
        for (UsuarioDTO usuario : usuarios.values()) {
            if (usuario.getUsername().equals(username) && usuario.getContrasena().equals(contrasena)) {
                System.out.println("Login exitoso para: " + username);

                return usuario;
            }
        }
        System.out.println("Login fallido para: " + username);
        return null;
    }

    public void logout(String token) { //He añadido esto para que haga el logout
        for (UsuarioDTO usu : usuarios.values()) {
            if (usu.getToken() != null && usu.getToken().equals(token)) { 
                usu.setToken(null);
                actualizarUsuario(usu);
                System.out.println("Usuario desconectado");
                return;
            }
        }
        System.out.println("Token no encontrado");
    }


    public void eliminarUsuario(UsuarioDTO usuario) {
        if (usuario != null) {
            usuarios.remove(usuario.getId());
            System.out.println("Usuario eliminado");
        }
    }

    //Usuario Service
    public void actualizarUsuario(UsuarioDTO usuarioDTO) {
        UsuarioDTO usuario = usuarios.get(usuarioDTO.getId());
        if (usuario != null) {
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setContrasena(usuarioDTO.getContrasena());
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setPeso(usuarioDTO.getPeso());
            usuario.setAltura(usuarioDTO.getAltura());
            usuario.setfNacimiento(usuarioDTO.getfNacimiento());

            // Evitar duplicados en la lista de amigos
            ArrayList<Integer> amigosActualizados = new ArrayList<>();
            for (Integer amigo : usuarioDTO.getAmigos()) {
                if (!amigosActualizados.contains(amigo)) {
                    amigosActualizados.add(amigo);
                }
            }
            usuario.setAmigos(amigosActualizados);

            usuario.setEntrenamientos(usuarioDTO.getEntrenamientos());
            usuario.setRetos(usuarioDTO.getRetos());

            usuarios.put(usuarioDTO.getId(), usuario);

            System.out.println("Usuario actualizado: " + usuario.getUsername());
        }
    }

    public UsuarioDTO obtenerUsuarioPorNombre(String username) {
        return usuarios.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }


    public void registrarUsuario(UsuarioDTO usuario) {
        if (usuarios.values().stream().anyMatch(u -> u.getUsername().equals(usuario.getUsername()))) {
            throw new IllegalArgumentException("Usuario ya existe: " + usuario.getUsername());
        }

        int newId = usuarios.size() + 1;
        usuario.setId(newId);
        usuarios.put(newId, usuario);
    }



    public static HashMap<Integer, UsuarioDTO> getUsuarios() {
        return usuarios;
    }

    public static void setUsuarios(HashMap<Integer, UsuarioDTO> usuarios) {
        UsuarioService.usuarios = usuarios;
    }

    public List<Integer> getAmigos(UsuarioDTO usuario){
        ArrayList<Integer> amigos= new ArrayList<>();
        for (Integer usu: usuario.getAmigos()) {
            amigos.add(usu);
        }

        return amigos;
    }

    public static UsuarioService getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioService();
        }
        return instancia;
    }



    public Map<Integer, Float> calcularProgreso(UsuarioDTO usuarioDTO) throws RemoteException {
        Map<Integer, Float> progresoPorReto = new HashMap<>();

        // Obtener retos y entrenamientos del usuario
        HashMap<RetoDTO, String> retos = usuarioDTO.getRetos();  // Map<Reto, Estado>
        List<EntrenamientoDTO> entrenamientos = usuarioDTO.getEntrenamientos();

        // Iterar sobre los retos
        for (RetoDTO reto : retos.keySet()) {
            double totalDistancia = 0;
            double totalDuracion = 0;

            // Filtrar entrenamientos relevantes basados en las fechas y el deporte del reto
            for (EntrenamientoDTO entrenamiento : entrenamientos) {
                if (entrenamiento.getDeporte().equalsIgnoreCase(reto.getDeporte())
                        && !entrenamiento.getFecIni().isBefore(reto.getFecIni().toLocalDate())
                        && !entrenamiento.getFecIni().isAfter(reto.getFecFin().toLocalDate())) {

                    totalDistancia += entrenamiento.getDistancia();
                    totalDuracion += entrenamiento.getDuracion();
                }
            }

            // Calcular el progreso basado en el objetivo de distancia y tiempo
            float progresoDistancia = (reto.getObjetivoDistancia() > 0)
                    ? (float) (totalDistancia / reto.getObjetivoDistancia()) * 100
                    : 0;

            float progresoTiempo = (reto.getObjetivoTiempo() > 0)
                    ? (float) (totalDuracion / reto.getObjetivoTiempo()) * 100
                    : 0;

            // Combina progreso de distancia y tiempo, eligiendo el valor más realista
            float progreso = Math.min(100, Math.max(progresoDistancia, progresoTiempo));

            // Almacenar progreso en el mapa
            progresoPorReto.put(reto.getId(), progreso);
        }

        return progresoPorReto;
    }




}