package com.strava.prueba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.server.*;

@Component
public class ClasePrueba {

    private final UsuarioRepository usuarioRepository;

    // Spring inyectará automáticamente UsuarioRepository en el constructor
    @Autowired
    public ClasePrueba(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void usarRepositorio() {
        // Usar el repositorio
        System.out.println("Accediendo al repositorio de usuarios: " + usuarioRepository);
    }
}