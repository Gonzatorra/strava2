package com.strava.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import com.strava.DTO.*;
import com.strava.dominio.*;
import com.strava.assembler.*;

public class EntrenamientoService {
    private static HashMap<Integer,Integer> entrenoIdxUsuario;

    public EntrenamientoService() {
        entrenoIdxUsuario = new HashMap<Integer, Integer>(); //IDusuario, IDultimoentreno
        System.out.println("UsuarioService inicializado.");
    }

    public EntrenamientoDTO crearEntreno(UsuarioDTO usuario, String titulo, String deporte, double distancia, LocalDate fechaIni,
                                         float horaInicio, double duracion) {
        // Crear el nuevo entrenamiento
        Integer idUEntreno=entrenoIdxUsuario.getOrDefault(usuario.getId(), 0); //si no tiene entrenamientos
        if(idUEntreno==null) {
            entrenoIdxUsuario.put(usuario.getId(), 0);
        }
        entrenoIdxUsuario.put(usuario.getId(), idUEntreno+1);
        EntrenamientoDTO entreno = new EntrenamientoDTO(idUEntreno+1, usuario.getUsername(), titulo, deporte, (float) distancia, fechaIni, horaInicio, duracion);
        return entreno;
    }


    public void actualizarEntreno(EntrenamientoDTO entrenamiento, UsuarioDTO usu, String titulo, String deporte, double distancia, double duracion) {
        for (EntrenamientoDTO e : usu.getEntrenamientos()) {
            if (e.getId() == entrenamiento.getId()) {
                e.setTitulo(titulo);
                e.setDeporte(deporte);
                e.setDistancia((float) distancia);
                e.setDuracion(duracion);

                System.out.println("Entrenamiento actualizado: " + titulo);
                return;  // Sale del método después de actualizar el entrenamiento
            }
        }

        // Mensaje fuera del bucle, solo si no se encuentra el entrenamiento
        System.out.println("No se encontró el entrenamiento para actualizar.");
    }



    public void eliminarEntreno(int index, EntrenamientoDTO entrenamiento) {
        System.out.println("Entrenamiento eliminado: " + entrenamiento.getTitulo());
    }

    public void visualizarEntreno(EntrenamientoDTO entrenamientoDTO) {
        System.out.println("Visualizando entrenamiento: " + entrenamientoDTO.getTitulo());
    }
}