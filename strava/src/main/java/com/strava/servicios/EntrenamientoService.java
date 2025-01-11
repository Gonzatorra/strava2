package com.strava.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import com.BD.dao.EntrenamientoDAO;
import com.BD.dao.UsuarioDAO;
import com.BD.entity.EntrenamientoEntity;
import com.BD.entity.UsuarioEntity;
import com.strava.DTO.*;
import com.strava.dominio.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import com.strava.assembler.*;

public class EntrenamientoService {
    private static HashMap<Integer,Integer> entrenoIdxUsuario;

    public EntrenamientoService() {
        entrenoIdxUsuario = new HashMap<Integer, Integer>(); //IDusuario, IDultimoentreno
        System.out.println("UsuarioService inicializado.");
    }

    public EntrenamientoDTO crearEntreno(UsuarioDTO usuario, String titulo, String deporte, double distancia, LocalDate fechaIni,
            float horaInicio, double duracion) {

		Integer idUEntreno = entrenoIdxUsuario.getOrDefault(usuario.getId(), 0); // si no tiene entrenamientos
		if (idUEntreno == null) {
		entrenoIdxUsuario.put(usuario.getId(), 0);
		}
		entrenoIdxUsuario.put(usuario.getId(), idUEntreno + 1);
		
		EntrenamientoDTO entreno = new EntrenamientoDTO(
		idUEntreno + 1,
		usuario.getUsername(),
		titulo,
		deporte,
		(float) distancia,
		fechaIni,
		horaInicio,
		duracion
		);
		
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntrenamientoDAO entrenamientoDAO = new EntrenamientoDAO(entityManager);
		
		EntrenamientoEntity entrenamientoBD = new EntrenamientoEntity();
		entrenamientoBD.setDistancia(distancia);
		entrenamientoBD.setDeporte(deporte);
		entrenamientoBD.setDuracion(duracion);
		entrenamientoBD.setFechaInicio(fechaIni);
		entrenamientoBD.setHoraInicio(horaInicio);
		entrenamientoBD.setUsuario(usuario.getUsername());
		entrenamientoBD.setTitulo(titulo);
		
		entrenamientoDAO.createEntrenamiento(entrenamientoBD);
		
		entreno.setId(entrenamientoBD.getId());
		
		return entreno;
    }



    public void actualizarEntreno(EntrenamientoDTO entrenamiento, UsuarioDTO usu, String titulo, String deporte, double distancia, double duracion) {
        for (EntrenamientoDTO e : usu.getEntrenamientos()) {
            if (e.getId() == entrenamiento.getId()) {
                e.setTitulo(titulo);
                e.setDeporte(deporte);
                e.setDistancia((float) distancia);
                e.setDuracion(duracion);
                
              //BD
            	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                EntrenamientoDAO entrenamientoDAO = new EntrenamientoDAO(entityManager);
                EntrenamientoEntity entrenamientoBD = new EntrenamientoEntity();
                entrenamientoBD.setDistancia(distancia);
                entrenamientoBD.setDeporte(deporte);
                entrenamientoBD.setDuracion(duracion);
                entrenamientoBD.setFechaInicio(entrenamiento.getFecIni());
                entrenamientoBD.setHoraInicio(entrenamiento.getHoraIni());
                entrenamientoBD.setId(entrenamiento.getId());
                entrenamientoBD.setUsuario(usu.getUsername());
                entrenamientoDAO.updateEntrenamiento(entrenamientoBD.getId(), entrenamientoBD);
            	//
                

                System.out.println("Entrenamiento actualizado: " + titulo);
                return;  // Sale del método después de actualizar el entrenamiento
            }
        }

        // Mensaje fuera del bucle, solo si no se encuentra el entrenamiento
        System.out.println("No se encontró el entrenamiento para actualizar.");
    }



    public void eliminarEntreno(int index, EntrenamientoDTO entrenamiento) {
    	
    	//BD
    	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntrenamientoDAO entrenamientoDAO = new EntrenamientoDAO(entityManager);
        entrenamientoDAO.deleteEntrenamiento(entrenamiento.getId());
        //
        
        System.out.println("Entrenamiento eliminado: " + entrenamiento.getTitulo());
    }

    public void visualizarEntreno(EntrenamientoDTO entrenamientoDTO) {
        System.out.println("Visualizando entrenamiento: " + entrenamientoDTO.getTitulo());
    }
    
    public void crearOActualizarEntrenamiento(EntrenamientoDTO e) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        EntrenamientoDAO dao = new EntrenamientoDAO(em);

        EntrenamientoEntity entrenoBD = new EntrenamientoEntity();
        entrenoBD.setId(e.getId());
        entrenoBD.setTitulo(e.getTitulo());
        entrenoBD.setFechaInicio(e.getFecIni());
        entrenoBD.setHoraInicio(e.getHoraIni());
        entrenoBD.setDistancia(e.getDistancia());
        entrenoBD.setDuracion(e.getDuracion());
        entrenoBD.setDeporte(e.getDeporte());
        entrenoBD.setUsuario(e.getUsuario());

        if (entrenoBD.getId() == 0) {
            dao.createEntrenamiento(entrenoBD);
        } else {
            dao.updateEntrenamiento(entrenoBD.getId(), entrenoBD);
        }
    }

}