package com.strava.prueba;

import com.google.server.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PruebaServer {
    private final UsuarioRepository usuarioRepository;

    // Spring inyectará automáticamente UsuarioRepository en el constructor
    @Autowired
    public PruebaServer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void usarRepositorio() {
        // Usar el repositorio
        System.out.println("Accediendo al repositorio de usuarios: " + usuarioRepository);
    }
}
