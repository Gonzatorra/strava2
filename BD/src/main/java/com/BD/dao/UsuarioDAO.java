package com.BD.dao;

import com.BD.entity.UsuarioEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class UsuarioDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public UsuarioDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //Metodo para crear un Usuario
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


    //Actualizar un Usuario
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


}
