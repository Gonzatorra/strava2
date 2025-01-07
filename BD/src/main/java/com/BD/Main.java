package com.BD;

import com.BD.entity.UsuarioEntity;
import com.BD.entity.RetoEntity;
import com.BD.entity.EntrenamientoEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        // Inicializar el EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        // Aquí va la lógica de tu aplicación

        em.close();
        emf.close();
        /*EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

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

            entityManager.persist(usuario);

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

            System.out.println("Bien!");

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/
    }
}
