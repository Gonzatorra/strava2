package com.strava.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.strava.DTO.*;
import com.strava.dominio.*;

public class UsuarioAssembler {

    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setContrasena(usuario.getContrasena());
        dto.setNombre(usuario.getNombre());
        dto.setPeso(usuario.getPeso());
        dto.setAltura(usuario.getAltura());
        dto.setfNacimiento(usuario.getfNacimiento());
        dto.setFecCMax(usuario.getFecCMax());
        dto.setFecCReposo(usuario.getFecCReposo());

        ArrayList<Integer> amigosDT = new ArrayList<Integer>();
        for (Integer uID: usuario.getAmigos()) {
            amigosDT.add(uID);
        }
        dto.setAmigos(amigosDT);

        dto.setToken(usuario.getToken());
        dto.setProveedor(usuario.getProveedor());


        ArrayList<EntrenamientoDTO> entrenosDT = new ArrayList<>();
        for (Entrenamiento e: usuario.getEntrenamientos()) {
            entrenosDT.add(EntrenamientoAssembler.toDTO(e));
        }
        dto.setEntrenamientos(entrenosDT);

        
        dto.setRetos(usuario.getRetos());




        return dto;
    }

    public static Usuario toDomain(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setContrasena(usuarioDTO.getContrasena());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setPeso(usuarioDTO.getPeso());
        usuario.setAltura(usuarioDTO.getAltura());
        usuario.setfNacimiento(usuarioDTO.getfNacimiento());
        usuario.setFecCMax(usuarioDTO.getFecCMax());
        usuario.setFecCReposo(usuarioDTO.getFecCReposo());
        usuario.setToken(usuarioDTO.getToken());
        usuario.setProveedor(usuarioDTO.getProveedor());

        ArrayList<Integer> amigosD = new ArrayList<>();
        for (Integer uID: usuarioDTO.getAmigos()) {
            amigosD.add(uID);
        }
        usuario.setAmigos(amigosD);

        usuario.setToken(usuarioDTO.getToken());
        usuario.setProveedor(usuarioDTO.getProveedor());


        ArrayList<Entrenamiento> entrenosD = new ArrayList<>();
        for (EntrenamientoDTO e: usuarioDTO.getEntrenamientos()) {
            entrenosD.add(EntrenamientoAssembler.toDomain(e));
        }
        usuario.setEntrenamientos(entrenosD);

        
        usuario.setRetos(usuarioDTO.getRetos());


        return usuario;
    }
}