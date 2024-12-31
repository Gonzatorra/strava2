package com.google.server;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    //Login: Verificar username y contraseña, y generar un token si son correctos
    public Usuario login(String username, String contrasena) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsername(username);
        Usuario usuario;

        if (usuarioExistente.isPresent()) {
            usuario = usuarioExistente.get();

            //Verificar la contraseña
            if (!usuario.getContrasena().equals(contrasena)) {
                throw new RuntimeException("Contraseña incorrecta");
            }
        } else {
            //Si no se encuentra el usuario, lanzar error
            throw new RuntimeException("Usuario no encontrado");
        }

        //Generar un nuevo token para cada inicio de sesion
        usuario.setToken(UUID.randomUUID().toString());
        return usuarioRepository.save(usuario);
    }

    //Logout: Verificar si el usuario tiene un token, y eliminarlo si es así
    public Optional<Usuario> logout(String username) {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        if (usuario.isPresent() && usuario.get().getToken() != null) {
            usuario.get().setToken(null);  //Eliminar el token
            usuarioRepository.save(usuario.get());  //Guardar el cambio
            return usuario;
        }
        return Optional.empty();
    }

    //Registro: Crear usuario con username, email y contraseña
    public Usuario register(String username, String email, String contrasena) {
        System.out.println("Llamando a register para el usuario " + username);
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsername(username);

        if (usuarioExistente.isPresent()) {
            System.out.println("El usuario ya existe: " + username);
            throw new RuntimeException("El usuario ya existe");
        }

        //Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);  //Añadir el email
        nuevoUsuario.setContrasena(contrasena);  //Añadir la contraseña

        System.out.println("Usuario " + username + " registrado");

        return usuarioRepository.save(nuevoUsuario);
    }

    //Verificar si un usuario existe por su username
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

}


