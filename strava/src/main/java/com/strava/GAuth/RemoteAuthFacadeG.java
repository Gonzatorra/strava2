package com.strava.GAuth;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.strava.DTO.*;
import com.strava.servicios.*;

public class RemoteAuthFacadeG implements IRemoteAuthFacadeG {

    static private  Map<String, String> userStore = new HashMap<>();
    static private  Map<String, String> tokenStore = new HashMap<>(); //tuplas de username, token. usuarios activos
    static private  Map<String, String> userInfoStore = new HashMap<>();
    private ServicioAutentificacion servicioAutenticacion;
    UsuarioService servicioUsu;

    public RemoteAuthFacadeG() throws RemoteException {}

    @Override
    public String registerUser(String username, String password, String email) throws RemoteException {
        if (userStore.containsKey(username)) {
            return "User already exists";
        }
        userStore.put(username, password);
        userInfoStore.put(username, username + ", " + email);
        return "User registered successfully";
    }

    @Override
    public String loginUser(String username, String password, String proveedor) throws RemoteException {
        if (userStore.containsKey(username) && userStore.get(username).equals(password)) {
            if(tokenStore.get(username)!= null )
            {
                String token= servicioAutenticacion.autenticar(username, password, "Google", proveedor);
                tokenStore.put(username, token);
                UsuarioDTO usuario= servicioUsu.obtenerUsuarioPorNombre(username);
                servicioUsu.actualizarUsuario(usuario);

                return token;
            }
            else {
                return tokenStore.get(username);
            }



        }
        return null;
    }

    @Override
    public void logout(String username) throws RemoteException {
        if (tokenStore.containsKey(username)) {
            tokenStore.remove(username);
            System.out.println("Token eliminado para usuario de Google: " + username);
        } else {
            System.out.println("No se encontró token para el usuario: " + username);
        }
    }

}