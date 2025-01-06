package com.BD.dao;

import com.strava.dominio.Reto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

public class RetoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createReto(Reto reto) {
        try {
            entityManager.persist(reto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Reto findRetoById(Long id) {
        return entityManager.find(Reto.class, id);
    }

    public List<Reto> findAllRetos() {
        TypedQuery<Reto> query = entityManager.createQuery(
            "SELECT r FROM Reto r", Reto.class
        );
        return query.getResultList();
    }

    @Transactional
    public void updateReto(Reto reto) {
        try {
            entityManager.merge(reto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteReto(Long id) {
        try {
            Reto reto = findRetoById(id);
            if (reto != null) {
                entityManager.remove(reto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
