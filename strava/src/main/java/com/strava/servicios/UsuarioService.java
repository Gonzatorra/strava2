package com.strava.servicios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.BD.dao.UsuarioDAO;
import com.BD.entity.UsuarioEntity;
import com.strava.DTO.*;
import com.strava.assembler.*;
import com.strava.dominio.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@SuppressWarnings("serial")
public class UsuarioService implements Serializable {

    public static HashMap<Integer, UsuarioDTO> usuarios = new HashMap<>();
    public static int idCounter = 1;  //para signar IDs unicos
    public static UsuarioService instancia;



    public UsuarioService() {
        usuarios = new HashMap<>();
        idCounter = 1;
        System.out.println("UsuarioService inicializado.");
    }

    public UsuarioDTO registrar(String username, String contrasena, String email, String nombre, String proveedor) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(entityManager);

        UsuarioEntity usuarioBD = new UsuarioEntity();
        usuarioBD.setUsername(username);
        usuarioBD.setEmail(email);
        usuarioBD.setContrasena(contrasena);
        usuarioBD.setNombre(nombre);
        usuarioBD.setProveedor(proveedor);
        usuarioBD.setAmigos(new ArrayList<>());
        usuarioDAO.createUsuario(usuarioBD);

        int nuevoId = idCounter++;
        Usuario usuario = new Usuario(nuevoId, username, email, contrasena, nombre, null, proveedor,
                new ArrayList<>(), new HashMap<>(), new ArrayList<>());
        usuarios.put(UsuarioAssembler.toDTO(usuario).getId(), UsuarioAssembler.toDTO(usuario));

        System.out.println("Usuario registrado: " + username);
        return UsuarioAssembler.toDTO(usuario);
    }
    
    public void borrarDeGetRetos(UsuarioDTO usu, RetoDTO reto) {
    	
    	usu.getRetos().remove(reto);
    	actualizarUsuario(usu);
    }

    public void logout(String token) {
    	UsuarioDTO usuario = usuarios.values().stream()
    	        .filter(u -> token.equals(u.getToken()))
    	        .findFirst()
    	        .orElse(null);
    	    if (usuario != null) {
    	        usuario.setToken(null);
    	        actualizarUsuario(usuario);
    	        System.out.println("Logout exitoso para: " + usuario.getUsername());
    	    } else {
    	        System.out.println("Token no encontrado");
    	    }
    }


    //Usuario Service
    public void actualizarUsuario(UsuarioDTO usuarioDTO) {
        UsuarioDTO usuario = usuarios.get(usuarioDTO.getId());
        if (usuario != null) {
        	
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            UsuarioDAO usuarioDAO = new UsuarioDAO(entityManager);

            UsuarioEntity usuarioBD = new UsuarioEntity();
            usuarioBD.setId(usuarioDTO.getId());
            usuarioBD.setUsername(usuarioDTO.getUsername());
            usuarioBD.setEmail(usuarioDTO.getEmail());
            usuarioBD.setContrasena(usuarioDTO.getContrasena());
            usuarioBD.setNombre(usuarioDTO.getNombre());
            usuarioBD.setProveedor(usuarioDTO.getProveedor());
            usuarioBD.setAmigos(usuarioDTO.getAmigos());
            usuarioBD.setToken(usuarioDTO.getToken());
            usuarioBD.setFrecCMax(usuarioDTO.getFecCMax());
            usuarioBD.setAltura(usuarioDTO.getAltura());
            usuarioBD.setfNacimiento(usuarioDTO.getfNacimiento());
            usuarioBD.setFrecCReposo(usuarioDTO.getFecCReposo());
            usuarioBD.setPeso(usuarioDTO.getPeso());

            usuarioDAO.updateUsuario(usuarioBD.getId(), usuarioBD);

            /*usuario.setUsername(usuarioDTO.getUsername());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setContrasena(usuarioDTO.getContrasena());
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setPeso(usuarioDTO.getPeso());
            usuario.setAltura(usuarioDTO.getAltura());
            usuario.setfNacimiento(usuarioDTO.getfNacimiento());
            usuario.setFecCMax(usuarioDTO.getFecCMax());
            usuario.setToken(usuarioDTO.getToken());
            usuario.setFecCReposo(usuarioDTO.getFecCReposo());
            usuario.setRetos(usuarioDTO.getRetos());
            usuario.setEntrenamientos(usuarioDTO.getEntrenamientos());
            usuario.setAmigos(usuarioDTO.getAmigos());*/
            
            usuarios.put(usuarioDTO.getId(), usuarioDTO);
            //usuarios.put(usuarioDTO.getId(), usuario);
            System.out.println("Usuario actualizado: " + usuario.getUsername());
        }
    }



    public UsuarioDTO obtenerUsuarioPorNombre(String username) {
    	System.out.println("Intentando obtener a: " + username);
        for (UsuarioDTO user : usuarios.values()) {
            if (user.getUsername().equals(username)) {
                System.out.println("Usuario encontrado: " + username);
                return user;
            }
        }
        System.err.println("Usuario no enocntrado en memoria: " + username);
        return null;
    }



    public static HashMap<Integer, UsuarioDTO> getUsuarios() {
        return usuarios;
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

}