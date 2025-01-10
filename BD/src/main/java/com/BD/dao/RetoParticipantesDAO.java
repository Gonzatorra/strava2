package com.BD.dao;

import com.BD.entity.RetoParticipantesEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

public class RetoParticipantesDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public RetoParticipantesDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void addParticipant(int usuarioId, int retoId, String estado) {
        RetoParticipantesEntity participant = new RetoParticipantesEntity();
        participant.setUsuarioId(usuarioId);
        participant.setRetoId(retoId);
        participant.setEstado(estado);

        entityManager.getTransaction().begin();
        entityManager.persist(participant);
        entityManager.getTransaction().commit();
    }

    public List<RetoParticipantesEntity> getParticipantsByRetoId(int retoId) {
        return entityManager.createQuery(
            "SELECT r FROM RetoParticipantesEntity r WHERE r.retoId = :retoId",
            RetoParticipantesEntity.class
        ).setParameter("retoId", retoId).getResultList();
    }

    public List<RetoParticipantesEntity> getRetosByUsuarioId(int usuarioId) {
        return entityManager.createQuery(
            "SELECT r FROM RetoParticipantesEntity r WHERE r.usuarioId = :usuarioId",
            RetoParticipantesEntity.class
        ).setParameter("usuarioId", usuarioId).getResultList();
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

    @Transactional
    public void removeParticipant(int usuarioId, int retoId) {
        RetoParticipantesEntity participant = entityManager.createQuery(
            "SELECT r FROM RetoParticipantesEntity r WHERE r.usuarioId = :usuarioId AND r.retoId = :retoId",
            RetoParticipantesEntity.class
        ).setParameter("usuarioId", usuarioId)
         .setParameter("retoId", retoId)
         .getSingleResult();

        entityManager.getTransaction().begin();
        entityManager.remove(participant);
        entityManager.getTransaction().commit();
    }
}
