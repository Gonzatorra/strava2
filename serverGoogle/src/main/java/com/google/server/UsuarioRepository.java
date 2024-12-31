package com.google.server;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UsuarioRepository {
    private final Map<String, Usuario> usuarios = new HashMap<>();

    public Optional<Usuario> findByUsername(String username) {
        return Optional.ofNullable(usuarios.get(username));
    }

    public Usuario save(Usuario usuario) {
        usuarios.put(usuario.getUsername(), usuario);  //Guardamos por username
        return usuario;
    }

    public void delete(String email) {
        usuarios.remove(email);
    }
}
