package com.BD.dao;

import com.strava.dominio.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class UsuarioDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void createUsuario(Usuario usuario) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(usuario);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public Usuario findUsuarioById(Long id) {
        return entityManager.find(Usuario.class, id);
    }

    public Usuario findUsuarioByUsername(String username) {
        TypedQuery<Usuario> query = entityManager.createQuery(
            "SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public List<Usuario> findAllUsuarios() {
        return entityManager.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public void updateUsuario(Usuario usuario) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(usuario);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void deleteUsuario(Long id) {
        try {
            entityManager.getTransaction().begin();
            Usuario usuario = entityManager.find(Usuario.class, id);
            if (usuario != null) {
                entityManager.remove(usuario);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
