package com.strava.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import com.strava.DTO.*;
import com.strava.assembler.*;
import com.strava.dominio.*;
import com.strava.assembler.*;

public class RetoService {
    private static HashMap<Integer,RetoDTO> retos;
    private static int idCounter = 1;  //para signar IDs unicos

    public RetoService() {
        retos = new HashMap<>();
        System.out.println("RetoService inicializado.");
    }

    public RetoDTO crearReto(String nombre, LocalDateTime fecIni, LocalDateTime fecFin, float objetivoDistancia, float objetivoTiempo,
                             String deporte, UsuarioDTO usuarioCreador, List<UsuarioDTO> participantes) {
        List<Usuario> particips= new ArrayList<Usuario>();
        for (UsuarioDTO usu: participantes) {
            particips.add(UsuarioAssembler.toDomain(usu));
        }

        int nuevoId = idCounter++;
        Usuario usu= UsuarioAssembler.toDomain(usuarioCreador);
        particips.add(usu);
        ArrayList<Integer> ids= new ArrayList<Integer>();
        for (Usuario u: particips) {
            ids.add(u.getId());
        }

        RetoDTO reto = RetoAssembler.toDTO(new Reto(nuevoId, deporte, usu.getUsername(), nombre, fecIni, fecFin, objetivoDistancia, objetivoTiempo, ids));
        UsuarioAssembler.toDomain(usuarioCreador).getRetos().put(RetoAssembler.toDomain(reto), "prueba");
        retos.put(nuevoId,reto);
        System.out.println("Reto creado: " + reto.getNombre());
        return reto;
    }

    public void aceptarReto(UsuarioDTO usuario, RetoDTO reto) {
        RetoAssembler.toDomain(reto).aceptarReto(UsuarioAssembler.toDomain(usuario));
    }

    public HashMap<Integer,RetoDTO> visualizarReto() {
        return retos;
    }

    public void actualizarReto(RetoDTO reto, String nombre, LocalDateTime fecIni, LocalDateTime fecFin, float objetivoDistancia, float objetivoTiempo,
                               String usuarioCreador, String deporte, ArrayList<Integer> participantes) {
        // Obtener el reto existente del mapa
        Reto retoExistente = RetoAssembler.toDomain(retos.get(reto.getId()));
        if (retoExistente != null && retoExistente.getUsuarioCreador().equals(usuarioCreador)) {


            // Actualizar los datos del reto existente
            retoExistente.actualizarReto(nombre, fecIni, fecFin, objetivoDistancia, objetivoTiempo, usuarioCreador, deporte,
                    participantes);

            // Actualizar el mapa (no es necesario si retoExistente ya está referenciado)
            retos.put(reto.getId(), RetoAssembler.toDTO(retoExistente));
        }
        else {
            JOptionPane.showMessageDialog(null, "No se ha podido actualizar el reto");
        }
    }

    public void eliminarReto(UsuarioDTO usuario, RetoDTO reto) {
        RetoAssembler.toDomain(reto).eliminarReto(UsuarioAssembler.toDomain(usuario));
        if(usuario.getUsername().equals(reto.getUsuarioCreador())) {
            retos.remove(reto.getId());

        }
        else {
            //retos.put(reto.getId(), reto);
        	/*
            RetoDTO r= retos.get(reto.getId());
            retos.put(r.getId(), r);
            */

        }
    }
    public List<Integer> obtenerClasificacion(RetoDTO reto) {

        return RetoAssembler.toDomain(reto).obtenerClasificacion();
    }

    public void calcularProgreso(UsuarioDTO usuario) {
        System.out.println("Calculando progreso del usuario: " + usuario.getUsername());
    }
}