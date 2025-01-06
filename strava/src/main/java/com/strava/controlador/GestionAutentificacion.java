package com.strava.controlador;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.rmi.RemoteException;
import com.strava.DTO.*;
import com.strava.fachada.*;

public class GestionAutentificacion {

    private RemoteFacade facade;

    // Constructor
    public GestionAutentificacion(RemoteFacade facade) {
        this.facade = facade;
    }

    // Método handleLogin
    private void handleLogin(String provider, String username, String password) {
        try {
            System.out.println("Proveedor: " + provider);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

            UsuarioDTO[] usuarioWrapper = new UsuarioDTO[1];

            if ("Google".equals(provider)) {
                usuarioWrapper[0] = facade.loginConProveedor(username, password, "Google");
            }

            if (usuarioWrapper[0] != null) {
                JOptionPane.showMessageDialog(null, "¡Inicio de sesión exitoso con " + provider + "!");
            } else {
                JOptionPane.showMessageDialog(null, "Autenticación fallida para " + provider + ".");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión con el servidor.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Llama al método handleLogin cuando el cliente solicita autenticación
    public void iniciarSesion(String proveedor, String username, String password) {
        handleLogin(proveedor, username, password);
    }
}