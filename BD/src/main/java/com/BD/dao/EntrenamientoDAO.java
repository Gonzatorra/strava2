package com.BD.dao;

import com.strava.dominio.Entrenamiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

public class EntrenamientoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createEntrenamiento(Entrenamiento entrenamiento) {
        try {
            entityManager.persist(entrenamiento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Entrenamiento findEntrenamientoById(Long id) {
        return entityManager.find(Entrenamiento.class, id);
    }

    public List<Entrenamiento> findAllEntrenamientos() {
        TypedQuery<Entrenamiento> query = entityManager.createQuery(
            "SELECT e FROM Entrenamiento e", Entrenamiento.class
        );
        return query.getResultList();
    }

    @Transactional
    public void updateEntrenamiento(Entrenamiento entrenamiento) {
        try {
            entityManager.merge(entrenamiento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteEntrenamiento(Long id) {
        try {
            Entrenamiento entrenamiento = findEntrenamientoById(id);
            if (entrenamiento != null) {
                entityManager.remove(entrenamiento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
