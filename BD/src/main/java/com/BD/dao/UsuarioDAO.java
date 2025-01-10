package com.BD.dao;

import com.BD.entity.RetoParticipantesEntity;
import com.BD.entity.UsuarioEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class UsuarioDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public UsuarioDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Método para crear un Usuario
    public void createUsuario(UsuarioEntity usuario) {
        try {
            entityManager.getTransaction().begin();
            ///entityManager.persist(usuario);
            entityManager.merge(usuario);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    // Buscar un Usuario por ID
    public UsuarioEntity findUsuarioById(Long id) {
        return entityManager.find(UsuarioEntity.class, id);
    }

    // Buscar un Usuario por Username
    public UsuarioEntity findUsuarioByUsername(String username) {
        TypedQuery<UsuarioEntity> query = entityManager.createQuery(
                "SELECT u FROM UsuarioEntity u WHERE u.username = :username", UsuarioEntity.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    // Obtener todos los usuarios
    public List<UsuarioEntity> findAllUsuarios() {
        return entityManager.createQuery("SELECT u FROM UsuarioEntity u", UsuarioEntity.class).getResultList();
    }

    // Actualizar un Usuario
    public void updateUsuario(long id, UsuarioEntity usuarioActualizado) {
        try {
            UsuarioEntity usuarioExistente = entityManager.find(UsuarioEntity.class, id);

            if (usuarioExistente != null) {
                usuarioExistente.setUsername(usuarioActualizado.getUsername());
                usuarioExistente.setEmail(usuarioActualizado.getEmail());
                usuarioExistente.setContrasena(usuarioActualizado.getContrasena());
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
                usuarioExistente.setPeso(usuarioActualizado.getPeso());
                usuarioExistente.setAltura(usuarioActualizado.getAltura());
                usuarioExistente.setfNacimiento(usuarioActualizado.getfNacimiento());
                usuarioExistente.setFrecCMax(usuarioActualizado.getFrecCMax());
                usuarioExistente.setFrecCReposo(usuarioActualizado.getFrecCReposo());
                usuarioExistente.setToken(usuarioActualizado.getToken());
                usuarioExistente.setProveedor(usuarioActualizado.getProveedor());
                usuarioExistente.setAmigos(usuarioActualizado.getAmigos());

                entityManager.getTransaction().begin();
                entityManager.merge(usuarioExistente);
                entityManager.getTransaction().commit();
            } else {
                System.out.println("Usuario no encontrado");
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }


    // Eliminar un Usuario por ID
    public void deleteUsuario(Long id) {
        try {
            entityManager.getTransaction().begin();
            UsuarioEntity usuario = entityManager.find(UsuarioEntity.class, id);
            if (usuario != null) {
                entityManager.remove(usuario);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
    
    public List<RetoParticipantesEntity> findRetosByUsuarioId(int usuarioId) {
        return entityManager.createQuery(
            "SELECT r FROM RetoParticipantesEntity r WHERE r.usuarioId = :usuarioId",
            RetoParticipantesEntity.class
        ).setParameter("usuarioId", usuarioId).getResultList();
    }

}
