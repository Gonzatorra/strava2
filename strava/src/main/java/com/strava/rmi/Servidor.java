package com.strava.rmi;

import com.meta.AuthClientMeta;
import com.strava.DTO.*;
import com.strava.config.ApplicationContextProvider;
import com.strava.fachada.*;
import com.google.server.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@Service
public class Servidor {
    private final RemoteFacade facade;
    private final GoogleAuthClient googleAuthClient;
    private final AuthClientMeta metaAuthClient;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public Servidor(UsuarioRepository usuarioRepository) throws RemoteException {
        this.usuarioRepository = usuarioRepository;
        this.googleAuthClient = new GoogleAuthClient(usuarioRepository);
        this.facade = new RemoteFacade(usuarioRepository);
        this.metaAuthClient = new AuthClientMeta("localhost", 1101);  // Inicialización directa en el constructor
    }

    public static void main(String[] args) {
        try {
            AnnotationConfigApplicationContext context = ApplicationContextProvider.getContext();
            System.out.println("Referencia compartida de context: " + context);

            Servidor servidor = context.getBean(Servidor.class);
            System.out.println("Referencia compartida de UsuarioRepository: " + servidor.usuarioRepository);

            // Crear o obtener el registro RMI
            Registry registry = null;
            try {
                registry = LocateRegistry.getRegistry(1099);
                registry.list();  // Intenta listar servicios, si lanza una excepción significa que no está en uso
                System.out.println("El registro RMI ya está en uso.");
            } catch (RemoteException e) {
                // Si no existe el registro, se crea
                System.out.println("Creando nuevo registro RMI.");
                registry = LocateRegistry.createRegistry(1099);
            }

            // Inicializar MetaAuthClient
            System.out.println("MetaAuthClient inicializado correctamente.");

            // Descartar exportación anterior si existe y exportar nuevamente
            IRemoteFacade stub = null;
            if (servidor.facade != null) {
                try {
                    UnicastRemoteObject.unexportObject(servidor.facade, true);
                    stub = (IRemoteFacade) UnicastRemoteObject.exportObject(servidor.facade, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            // Registrar el stub en el registro RMI
            registry.rebind("RemoteFacade", stub);

            // Usar el stub en el servidor (referenciar el objeto remoto)
            IRemoteFacade remoteFacade = (IRemoteFacade) registry.lookup("RemoteFacade");

            servidor.registrarUsuariosGoogle();
            servidor.registrarUsuariosMeta();
            servidor.registrarUsuariosYRetos();

        } catch (Exception e) {
            e.printStackTrace();
        }
}

    private void registrarUsuariosGoogle() {
        googleAuthClient.registerUser("daniel333", "claveDaniel", "daniel333@gmail.com");
        googleAuthClient.registerUser("susana555", "claveSusana", "susana555@gmail.com");
        googleAuthClient.registerUser("manuel111", "claveManuel", "manuel111@gmail.com");
        googleAuthClient.registerUser("isabel999", "claveIsabel", "isabel999@gmail.com");
        googleAuthClient.registerUser("andres444", "claveAndres", "andres444@gmail.com");
        googleAuthClient.registerUser("clara777", "claveClara", "clara777@gmail.com");
        googleAuthClient.registerUser("pablo888", "clavePablo", "pablo888@gmail.com");
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
        facade.registrarUsuario("carlos888", "carlosKey", "carlos888@strava.com", "Carlos", "Strava");

    }

    public void verRepositorio() {
        // Usar el repositorio
        System.out.println("Accediendo al repositorio de usuarios: " + usuarioRepository);
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }
}