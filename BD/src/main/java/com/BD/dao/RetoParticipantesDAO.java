package com.BD.dao;

import com.BD.entity.RetoParticipantesEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class RetoParticipantesDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public RetoParticipantesDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void updateEstado(int usuarioId, int retoId, String newEstado) {
        RetoParticipantesEntity participant = entityManager.createQuery(
            "SELECT r FROM RetoParticipantesEntity r WHERE r.usuarioId = :usuarioId AND r.retoId = :retoId",
            RetoParticipantesEntity.class
        ).setParameter("usuarioId", usuarioId)
         .setParameter("retoId", retoId)
         .getSingleResult();

        entityManager.getTransaction().begin();
        participant.setEstado(newEstado);
        entityManager.merge(participant);
        entityManager.getTransaction().commit();
    }

}
