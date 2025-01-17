package com.BD.dao;

import com.BD.entity.RetoEntity;
import com.BD.entity.RetoParticipantesEntity;
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

    @Transactional
    public void updateReto(long id, RetoEntity retoActualizado) {
        try {
            RetoEntity retoExistente = entityManager.find(RetoEntity.class, id);
            if (retoExistente != null) {
                retoExistente.setDeporte(retoActualizado.getDeporte());
                retoExistente.setFecFin(retoActualizado.getFecFin());
                retoExistente.setObjetivoDistancia(retoActualizado.getObjetivoDistancia());
                retoExistente.setObjetivoTiempo(retoActualizado.getObjetivoTiempo());
                retoExistente.setNombre(retoActualizado.getNombre());
                retoExistente.setFecIni(retoActualizado.getFecIni());

                entityManager.getTransaction().begin();
                entityManager.merge(retoExistente);
                entityManager.getTransaction().commit();
            } else {
                System.out.println("Reto no encontrado");
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

    @Transactional
    public void addParticipantToReto(int usuarioId, int retoId, String estado) {
        RetoParticipantesEntity participante = new RetoParticipantesEntity();
        participante.setUsuarioId(usuarioId);
        participante.setRetoId(retoId);
        participante.setEstado(estado);

        entityManager.getTransaction().begin();
        entityManager.persist(participante);
        entityManager.getTransaction().commit();
    }
/*
    @Transactional
    public void updateParticipantEstado(int usuarioId, int retoId, String estado) {
        RetoParticipantesEntity participante = entityManager.createQuery(
            "SELECT r FROM RetoParticipantesEntity r WHERE r.usuarioId = :usuarioId AND r.retoId = :retoId",
            RetoParticipantesEntity.class
        ).setParameter("usuarioId", usuarioId)
         .setParameter("retoId", retoId)
         .getSingleResult();

        entityManager.getTransaction().begin();
        participante.setEstado(estado);
        entityManager.merge(participante);
        entityManager.getTransaction().commit();
    }
*/
    @Transactional
    public void removeParticipantFromReto(int usuarioId, int retoId) {
        RetoParticipantesEntity participante = entityManager.createQuery(
            "SELECT r FROM RetoParticipantesEntity r WHERE r.usuarioId = :usuarioId AND r.retoId = :retoId",
            RetoParticipantesEntity.class
        ).setParameter("usuarioId", usuarioId)
         .setParameter("retoId", retoId)
         .getSingleResult();

        entityManager.getTransaction().begin();
        entityManager.remove(participante);
        entityManager.getTransaction().commit();
    }
    
}
