package com.BD.dao;

import com.BD.entity.EntrenamientoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class EntrenamientoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public EntrenamientoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void createEntrenamiento(EntrenamientoEntity entrenamientoEntity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entrenamientoEntity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
   
    @Transactional
    public void updateEntrenamiento(long id, EntrenamientoEntity entrenamientoActualizado) {
        try {
            EntrenamientoEntity entrenamientoExistente = entityManager.find(EntrenamientoEntity.class, id);
            if (entrenamientoExistente != null) {
                entrenamientoExistente.setDeporte(entrenamientoActualizado.getDeporte());
                entrenamientoExistente.setDistancia(entrenamientoActualizado.getDistancia());
                entrenamientoExistente.setDuracion(entrenamientoActualizado.getDuracion());
                entrenamientoExistente.setTitulo(entrenamientoActualizado.getTitulo());
                entrenamientoExistente.setFechaInicio(entrenamientoActualizado.getFechaInicio());
                entrenamientoExistente.setHoraInicio(entrenamientoActualizado.getHoraInicio());
                entrenamientoExistente.setUsuario(entrenamientoActualizado.getUsuario());
                
                entityManager.getTransaction().begin();
                entityManager.merge(entrenamientoExistente);
                entityManager.getTransaction().commit();
            } else {
                System.out.println("Entrenamiento no encontrado");
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteEntrenamiento(long id) {
        try {
            entityManager.getTransaction().begin();
            EntrenamientoEntity entrenamientoEntity = entityManager.find(EntrenamientoEntity.class, id);
            if (entrenamientoEntity != null) {
                entityManager.remove(entrenamientoEntity);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
}

