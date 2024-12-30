package com.strava.servicios;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import com.meta.AuthClientMeta;
import com.strava.GAuth.*;
import com.meta.*;

public class ServicioExternosBridge {
    private static IRemoteAuthFacadeG fachadaExternaG;
    private static AuthClientMeta metaAuthClient;

    public ServicioExternosBridge() {
        // Conecta al registro RMI en los puertos correspondientes
        Registry registry;
        try {
            // Conexión al registro RMI en el puerto 1100
            registry = LocateRegistry.getRegistry("localhost", 1100);
            if (fachadaExternaG != null) {
                // Descartar exportación anterior si la hay
                UnicastRemoteObject.unexportObject(fachadaExternaG, true);
                System.out.println("Exportación anterior de fachadaExternaG descartada.");
            }
            // Buscar y asignar el objeto remoto
            fachadaExternaG = (IRemoteAuthFacadeG) registry.lookup("RemoteAuthFacadeG");
            System.out.println("Conexión exitosa al RemoteAuthFacade en el puerto 1100.");

            // Inicialización de MetaAuthClient (no es remoto)
            metaAuthClient = new AuthClientMeta("localhost", 1101);
            System.out.println("Conexión exitosa a MetaAuthClient en el puerto 1101.");

        } catch (RemoteException e) {
            System.err.println("Error al conectar con el registro RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error en la inicialización: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("ServicioExternosBridge inicializado.");
    }

    public String verifyGoogle(String username, String pass, String proveedor) {
        try {
            String token= fachadaExternaG.loginUser(username, pass, proveedor);
            return token;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String verifyMeta(String username, String pass, String proveedor) throws IOException {
        try {
            String token = metaAuthClient.login(username, pass);
            return token;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logoutGoogle(String username) {
        try {
            fachadaExternaG.logout(username);
            System.out.println("Logout en Google exitoso para: " + username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void logoutMeta(String username) throws IOException {
        try {
            metaAuthClient.logout(username);
            System.out.println("Logout en Meta exitoso para: " + username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}