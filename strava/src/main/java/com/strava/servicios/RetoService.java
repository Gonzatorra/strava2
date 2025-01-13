package com.strava.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import com.BD.dao.RetoDAO;
import com.BD.dao.RetoParticipantesDAO;
import com.BD.dao.UsuarioDAO;
import com.BD.entity.RetoEntity;
import com.BD.entity.RetoParticipantesEntity;
import com.BD.entity.UsuarioEntity;
import com.strava.DTO.*;
import com.strava.assembler.*;
import com.strava.dominio.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import com.strava.assembler.*;

public class RetoService {
    private static HashMap<Integer,RetoDTO> retos;
    private static int idCounter = 1;  //para signar IDs unicos

    public RetoService() {
        retos = new HashMap<>();
        System.out.println("RetoService inicializado.");
    }
    /*
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
        //BD
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RetoDAO retoDAO = new RetoDAO(entityManager);
        RetoEntity retoBD = new RetoEntity();
        retoBD.setDeporte(deporte);
        retoBD.setFecFin(fecFin);
        retoBD.setFecIni(fecIni);
        retoBD.setId(nuevoId);
        retoBD.setNombre(nombre);
        retoBD.setObjetivoDistancia(objetivoDistancia);
        retoBD.setObjetivoTiempo(objetivoTiempo);
        retoBD.setParticipantes(ids);
        retoBD.setUsuarioCreador(usuarioCreador.getUsername());
        
        retoDAO.createReto(retoBD);
        //
        
        RetoDTO reto = RetoAssembler.toDTO(new Reto(nuevoId, deporte, usu.getUsername(), nombre, fecIni, fecFin, objetivoDistancia, objetivoTiempo, ids));
        UsuarioAssembler.toDomain(usuarioCreador).getRetos().put(RetoAssembler.toDomain(reto), "prueba");
        retos.put(nuevoId,reto);
        System.out.println("Reto creado: " + reto.getNombre());
        return reto;
    } */
    
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
    	
    	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		RetoDAO retoDAO = new RetoDAO(entityManager);

		RetoEntity retoBD = new RetoEntity();
		retoBD.setDeporte(deporte);
		retoBD.setFecFin(fecFin);
		retoBD.setFecIni(fecIni);
		retoBD.setNombre(nombre);
		retoBD.setObjetivoDistancia(objetivoDistancia);
		retoBD.setObjetivoTiempo(objetivoTiempo);
		retoBD.setUsuarioCreador(usuarioCreador.getUsername());
		retoBD.setId(nuevoId);
		
		retoDAO.createReto(retoBD);
		
		for (UsuarioDTO participante : participantes) {
			retoDAO.addParticipantToReto(participante.getId(), retoBD.getId(), "En Progreso");
		}
		Reto r= new Reto(retoBD.getId(), deporte, usuarioCreador.getUsername(), nombre, fecIni, fecFin,
				objetivoDistancia, objetivoTiempo, ids);
		retos.put(r.getId(), RetoAssembler.toDTO(r));
		
		
		
		return RetoAssembler.toDTO(r);
	}


    public void aceptarReto(UsuarioDTO usuario, RetoDTO reto) {
    	ArrayList<Integer> ids = reto.getParticipantes();
    	ids.add(usuario.getId());
    	actualizarReto(reto, reto.getNombre(), reto.getFecIni(), reto.getFecFin(), reto.getObjetivoDistancia(), reto.getObjetivoTiempo(), 
    			reto.getUsuarioCreador(), reto.getDeporte(), ids);
        RetoAssembler.toDomain(reto).aceptarReto(UsuarioAssembler.toDomain(usuario));
        
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
	    EntityManager entityManager = entityManagerFactory.createEntityManager();
	    RetoParticipantesDAO retopartiDAO = new RetoParticipantesDAO(entityManager);
        retopartiDAO.addParticipant(usuario.getId(), reto.getId(), "En Progreso");
        
    }

    public HashMap<Integer,RetoDTO> visualizarReto() {
        return retos;
    }

    public void actualizarReto(RetoDTO reto, String nombre, LocalDateTime fecIni, LocalDateTime fecFin,
            float objetivoDistancia, float objetivoTiempo,
            String usuarioCreador, String deporte, ArrayList<Integer> participantes) {
			Reto retoExistente = RetoAssembler.toDomain(retos.get(reto.getId()));
		if (retoExistente != null && retoExistente.getUsuarioCreador().equals(usuarioCreador)) {
		
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			RetoDAO retoDAO = new RetoDAO(entityManager);
			
			RetoEntity retoBD = new RetoEntity();
			retoBD.setDeporte(deporte);
			retoBD.setFecFin(fecFin);
			retoBD.setFecIni(fecIni);
			retoBD.setId(reto.getId());
			retoBD.setNombre(nombre);
			retoBD.setObjetivoDistancia(objetivoDistancia);
			retoBD.setObjetivoTiempo(objetivoTiempo);
			retoBD.setUsuarioCreador(usuarioCreador);
			
	
			retoDAO.updateReto(retoBD.getId(), retoBD);
			

			retoExistente.actualizarReto(nombre, fecIni, fecFin, objetivoDistancia, objetivoTiempo,
					usuarioCreador, deporte, participantes);
			retos.put(reto.getId(), RetoAssembler.toDTO(retoExistente));
		} else {
			JOptionPane.showMessageDialog(null, "No se ha podido actualizar el reto");
		}
    }


    public void eliminarReto(UsuarioDTO usuario, RetoDTO reto) {
        RetoAssembler.toDomain(reto).eliminarReto(UsuarioAssembler.toDomain(usuario));
        if(usuario.getUsername().equals(reto.getUsuarioCreador())) {
        	 EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
             EntityManager entityManager = entityManagerFactory.createEntityManager();
             RetoDAO retoDAO = new RetoDAO(entityManager);
             retoDAO.deleteReto((long) reto.getId());
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

    
    public void cambiarEstado(UsuarioDTO usuario, RetoDTO reto, String estado) {
        System.out.println("Cambiando estado del usuario: " + usuario.getUsername() + " en el reto: " + reto.getNombre() + " a: " + estado);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
	    EntityManager entityManager = entityManagerFactory.createEntityManager();
	    RetoParticipantesDAO retopartiDAO = new RetoParticipantesDAO(entityManager);
        retopartiDAO.updateEstado(usuario.getId(), reto.getId(), estado);
        
        
    }
    
    public void agregarParticipanteAReto(int usuarioId, int retoId, String estado) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RetoDAO retoDAO = new RetoDAO(entityManager);

        retoDAO.addParticipantToReto(usuarioId, retoId, estado);
    }
}