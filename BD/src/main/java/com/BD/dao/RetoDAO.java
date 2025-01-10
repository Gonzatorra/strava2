package com.BD.dao;

import com.BD.entity.RetoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

public class RetoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public RetoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void createReto(RetoEntity retoEntity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(retoEntity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public List<RetoEntity> findRetoByCreador(String creador) {
        TypedQuery<RetoEntity> query = entityManager.createQuery(
            "SELECT r FROM RetoEntity r WHERE r.creador = :creador",
            RetoEntity.class
        );
        query.setParameter("creador", creador);
        return query.getResultList();
    }

    public RetoEntity findRetoById(int id) {
        return entityManager.find(RetoEntity.class, id);
    }

    public List<RetoEntity> findAllRetos() {
        return entityManager.createQuery("SELECT r FROM RetoEntity r", RetoEntity.class).getResultList();
    }

    @Transactional
    public void updateReto(long id, RetoEntity retoActualizado) {
        try {
            RetoEntity retoExistente = entityManager.find(RetoEntity.class, id);
            if (retoExistente != null) {
                retoExistente.setDeporte(retoActualizado.getDeporte());
                retoExistente.setFecFin(retoActualizado.getFecFin());
                retoExistente.setObjetivoDistancia(retoActualizado.getObjetivoDistancia());
                retoExistente.setObjetivoTiempo(retoActualizado.getObjetivoTiempo());
                retoExistente.setParticipantes(retoActualizado.getParticipantes());
                retoExistente.setNombre(retoActualizado.getNombre());
                retoExistente.setFecIni(retoActualizado.getFecIni());

                // Persistir los cambios del usuario
                entityManager.getTransaction().begin();
                entityManager.merge(retoExistente);
                entityManager.getTransaction().commit();
            } else {
                System.out.println("Retoo no encontrado");
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteReto(Long id) {
        try {
            entityManager.getTransaction().begin();
            RetoEntity reto = entityManager.find(RetoEntity.class, id);
            if (reto != null) {
                entityManager.remove(reto);
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
