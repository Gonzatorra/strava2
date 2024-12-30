package com.google.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class AuthService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    public String loginUser(String username, String password, String proveedor) {
        // Buscar usuario en la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username);

        // Verificar si el usuario existe y si la contraseña es correcta
        if (usuario != null && usuario.getContrasena().equals(password)) {
            // Si el usuario tiene un token existente, devolvemos ese token
            if (usuario.getToken() != null) {
                return usuario.getToken();
            } else {
                // Si el usuario no tiene un token, generamos uno nuevo
                String token = UUID.randomUUID().toString();
                usuario.setToken(token); // Asignamos el nuevo token al usuario
                usuarioRepository.save(usuario); // Guardamos el usuario con el nuevo token
                return token;
            }
        }

        // Si las credenciales no son validas
        return null;
    }

    public void logout(String username) {
        // Buscar al usuario en la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario != null) {
            // Eliminar el token del usuario al hacer logout
            usuario.setToken(null);
            usuarioRepository.save(usuario); // Guardamos los cambios en la base de datos
            System.out.println("Token eliminado para el usuario: " + username);
        } else {
            System.out.println("No se encontró el usuario: " + username);
        }
    }
}
