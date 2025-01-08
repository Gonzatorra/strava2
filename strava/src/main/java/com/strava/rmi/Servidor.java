package com.strava.rmi;

import com.meta.AuthClientMeta;
import com.strava.DTO.*;
import com.strava.config.AppConfig;
import com.strava.fachada.*;
import com.google.server.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class Servidor {
    private final RemoteFacade facade;
    private final AuthClientMeta metaAuthClient;
    private final GoogleAuthClient googleAuthClient; // Añadimos esta propiedad para el cliente de Google

    public Servidor(ApplicationContext context) throws RemoteException {
        this.googleAuthClient = context.getBean(GoogleAuthClient.class); // Obtener el GoogleAuthClient desde el contexto de Spring
        this.facade = new RemoteFacade(context);
        this.metaAuthClient = new AuthClientMeta("localhost", 1101);
        iniciarRMI();
        try {
			registrarUsuariosMeta();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        registrarUsuariosYRetos();

        System.out.println("Servidor iniciado");
    }

    private void iniciarRMI() {
        try {
        	System.setProperty("java.rmi.server.hostname", "localhost");
        	Registry registry = LocateRegistry.createRegistry(1099);
        	registry.rebind("RemoteFacade", facade);
        	System.out.println("Servidor RMI registrado correctamente en el puerto 1099.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void registrarUsuariosMeta() throws IOException {
        metaAuthClient.registerUser("maria123", "claveMaria", "maria123@meta.com");
        metaAuthClient.registerUser("jose456", "claveJose", "jose456@meta.com");
        metaAuthClient.registerUser("lucia789", "claveLucia", "lucia789@meta.com");
        metaAuthClient.registerUser("carlos111", "claveCarlos", "carlos111@meta.com");
        metaAuthClient.registerUser("ana222", "claveAna", "ana222@meta.com");
        metaAuthClient.registerUser("david333", "claveDavid", "david333@meta.com");
        metaAuthClient.registerUser("laura444", "claveLaura", "laura444@meta.com");
    }


    private void registrarUsuariosYRetos() throws RemoteException {
        // Registrar usuarios y crear entrenamientos y retos
        LocalDate fecha = LocalDate.of(2024, 8, 23);
        LocalDateTime fecha1 = LocalDateTime.now();
        LocalDateTime fecha2 = LocalDateTime.of(2024, 8, 23, 0, 0);

        // Usuario 1
        UsuarioDTO usuario1 = facade.registrarUsuario("ana123", "hola", "ana123@strava.com", "Ana", "Strava");
        EntrenamientoDTO entreno1 = facade.crearEntreno(usuario1, "MiPrimerEntrenamiento", "running", 10.0, fecha, 14.5f, 0.0f);
        List<UsuarioDTO> challengers = new ArrayList<>();
        challengers.add(usuario1);
        RetoDTO reto1 = facade.crearReto("PrimerReto", fecha2, fecha1, 10, 30, "running", usuario1, challengers);
        usuario1.getEntrenamientos().add(entreno1);
        usuario1.getRetos().put(reto1, "superado");
        facade.actualizarUsuario(usuario1);

        // Usuario 2
        UsuarioDTO usuario2 = facade.registrarUsuario("juan456", "pass123", "juan456@strava.com", "Juan", "Strava");
        EntrenamientoDTO entreno2 = facade.crearEntreno(usuario2, "EntrenoAvanzado", "cycling", 20.0, fecha, 18.0f, 5.0f);
        List<UsuarioDTO> challengers2 = new ArrayList<>();
        challengers2.add(usuario2);
        RetoDTO reto2 = facade.crearReto("RetoCiclismo", fecha1, fecha2, 20, 50, "cycling", usuario2, challengers2);
        usuario2.getEntrenamientos().add(entreno2);
        usuario2.getRetos().put(reto2, "superado");
        facade.actualizarUsuario(usuario2);

        // Usuario 3
        UsuarioDTO usuario3 = facade.registrarUsuario("lucia789", "luciaPass", "lucia789@strava.com", "Lucía", "Strava");
        EntrenamientoDTO entreno3 = facade.crearEntreno(usuario3, "EntrenoMatutino", "swimming", 5.0, fecha, 12.0f, 1.0);
        List<UsuarioDTO> challengers3 = new ArrayList<UsuarioDTO>();
        challengers3.add(usuario3);
        RetoDTO reto3 = facade.crearReto("RetoNatacion", fecha1, fecha2, 5, 15, "swimming", usuario3, challengers3);
        usuario3.getEntrenamientos().add(entreno3);
        usuario3.getRetos().put(reto3, "pendiente");
        facade.actualizarUsuario(usuario3);

        // Usuarios 4 al 10
        facade.registrarUsuario("mario001", "marioKey", "mario001@strava.com", "Mario", "Strava");
        facade.registrarUsuario("elena345", "elenaKey", "elena345@strava.com", "Elena", "Strava");
        facade.registrarUsuario("pedro654", "pedroKey", "pedro654@strava.com", "Pedro", "Strava");
        facade.registrarUsuario("laura999", "lauraKey", "laura999@strava.com", "Laura", "Strava");
        facade.registrarUsuario("david111", "davidKey", "david111@strava.com", "David", "Strava");
        facade.registrarUsuario("sofia777", "sofiaKey", "sofia777@strava.com", "Sofía", "Strava");
        //facade.registrarUsuario("carlos888", "carlosKey", "carlos888@strava.com", "Carlos", "Strava");

    }

    public GoogleAuthClient getGoogleAuthClient() {
        return googleAuthClient;
    }

    public static void main(String[] args) {
        try {
            //Iniciar el contexto de Spring manualmente
            ApplicationContext context = SpringApplication.run(AppConfig.class, args);
            //Crear y ejecutar el servidor
            Servidor servidor = new Servidor(context);
            GoogleAuthClient googleAuthClient1 = servidor.getGoogleAuthClient();
            List<Usuario> usuariosDeGoogle = googleAuthClient1.allUsers();
            // Imprimir los usuarios
            if (usuariosDeGoogle != null) {
                System.out.println("Usuarios obtenidos de Google:");
                for (Usuario usuario : usuariosDeGoogle) {
                    System.out.println("Usuario: " + usuario.getUsername() + ", Correo: " + usuario.getEmail());
                }
            } else {
                System.out.println("No se pudieron obtener los usuarios.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}