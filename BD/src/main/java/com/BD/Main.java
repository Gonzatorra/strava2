package com.BD;

import com.BD.dao.UsuarioDAO;
import com.BD.entity.UsuarioEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO(entityManager);

            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setUsername("anamari");
            usuario.setEmail("anamari@martikorena.eus");
            usuario.setContrasena("1234");
            usuario.setNombre("Ana Maria Martikorena");
            usuario.setPeso(70.5f);
            usuario.setAltura(175.0f);
            usuario.setfNacimiento(new java.util.Date());
            usuario.setFrecCMax(180);
            usuario.setFrecCReposo(60);
            usuario.setToken("abc123");
            usuario.setProveedor("Strava");
            usuario.setAmigos(new ArrayList<>());
            usuario.setRetos(new HashMap<>());
            //usuarioDAO.createUsuario(usuario);

            UsuarioEntity usuario2 = new UsuarioEntity();
            usuario2.setUsername("aa");
            usuario2.setEmail("aa@ewa.eus");
            usuario2.setContrasena("1234");
            usuario2.setNombre("Ana Maria Martikorena");
            usuario2.setPeso(70.5f);
            usuario2.setAltura(175.0f);
            usuario2.setfNacimiento(new java.util.Date());
            usuario2.setFrecCMax(180);
            usuario2.setFrecCReposo(60);
            usuario2.setToken("abc123");
            usuario2.setProveedor("Strava");
            usuario2.setAmigos(new ArrayList<>());
            usuario2.setRetos(new HashMap<>());
            usuarioDAO.updateUsuario(1L, usuario2);


            //entityManager.persist(usuario);
/*
            RetoEntity reto = new RetoEntity();
            reto.setDeporte("Running");
            reto.setNombre("Marathon Challenge");
            reto.setFecIni(LocalDateTime.now());
            reto.setFecFin(LocalDateTime.now().plusMonths(1));
            reto.setObjetivoDistancia(42.195f);
            reto.setObjetivoTiempo(240.0f);
            reto.setUsuarioCreador(usuario);
            reto.setParticipantes(new ArrayList<>());

            usuario.getRetosCreados().add(reto);

            entityManager.persist(reto);

            EntrenamientoEntity entrenamiento = new EntrenamientoEntity();
            entrenamiento.setUsuario(usuario);
            entrenamiento.setTitulo("Morning Run");
            entrenamiento.setDeporte("Running");
            entrenamiento.setDistancia(10.0);
            entrenamiento.setDuracion(60.0);
            entrenamiento.setFechaInicio(LocalDateTime.now());
            entrenamiento.setHoraInicio(7.5f);

            usuario.getEntrenamientos().add(entrenamiento);

            entityManager.persist(entrenamiento);

            entityManager.getTransaction().commit();

            System.out.println("Bien!");*/

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManagerFactory.close();
        }
    }
}
