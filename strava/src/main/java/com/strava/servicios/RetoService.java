package com.strava.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import com.BD.dao.RetoDAO;
import com.BD.dao.RetoParticipantesDAO;
import com.BD.entity.RetoEntity;
import com.BD.entity.RetoParticipantesEntity;
import com.strava.DTO.*;
import com.strava.assembler.*;
import com.strava.dominio.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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
    	
    	

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		RetoDAO retoDAO = new RetoDAO(entityManager);
        

        

		retoDAO.addParticipantToReto(usuario.getId(), reto.getId(), "En Progreso");
        
    }

    public HashMap<Integer,RetoDTO> visualizarReto() {
        return retos;
    }
    

	public void actualizarReto(RetoDTO reto, String nombre, LocalDateTime fecIni, LocalDateTime fecFin,
            float objetivoDistancia, float objetivoTiempo,
            String usuarioCreador, String deporte, ArrayList<Integer> participantes) {
			Reto retoExistente = RetoAssembler.toDomain(retos.get(reto.getId()));
		if (retoExistente != null) {
		
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
			

			Reto reto2= retoExistente.actualizarReto(nombre, fecIni, fecFin, objetivoDistancia, objetivoTiempo,
					usuarioCreador, deporte, participantes);
			retos.put(reto2.getId(), RetoAssembler.toDTO(reto2));
		} else {
			JOptionPane.showMessageDialog(null, "No se ha podido actualizar el reto");
		}
    }

    public void eliminarReto(UsuarioDTO usuario, RetoDTO reto) {
        if(usuario.getUsername().equals(reto.getUsuarioCreador())) {
        	 EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
             EntityManager entityManager = entityManagerFactory.createEntityManager();
             RetoDAO retoDAO = new RetoDAO(entityManager);
             retoDAO.deleteReto((long) reto.getId());
             retos.remove(reto.getId());

        }
        else {
        	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            RetoDAO retoDAO = new RetoDAO(entityManager);
            retoDAO.removeParticipantFromReto(usuario.getId(), reto.getId());
            ArrayList<Integer> participantes= new ArrayList<Integer>();
            for(int i: reto.getParticipantes()){
            	if(i!=usuario.getId()) {
            		participantes.add(i);
            	}
            }
            reto.setParticipantes(participantes);
            
            retos.put(reto.getId(), reto);
        }
    }

    public void cambiarEstado(UsuarioDTO usuario, RetoDTO reto, String estado) {
        System.out.println("Cambiando estado del usuario: " + usuario.getUsername() + " en el reto: " + reto.getNombre() + " a: " + estado);
        
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");
	    EntityManager entityManager = entityManagerFactory.createEntityManager();
	    RetoParticipantesDAO retopartiDAO = new RetoParticipantesDAO(entityManager);
        retopartiDAO.updateEstado(usuario.getId(), reto.getId(), estado);
	    
	    RetoParticipantesEntity RetoPartDB = new RetoParticipantesEntity();
	    RetoPartDB.setRetoId(reto.getId());
	    RetoPartDB.setUsuarioId(usuario.getId());
	    RetoPartDB.setEstado(estado);
	    
        
        
    }

}