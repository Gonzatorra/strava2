package com.strava.prueba;

import com.google.server.UsuarioRepository;
import com.strava.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main2App {
    public static void main(String[] args) {
        // Iniciar el contexto de Spring
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Obtener las instancias de los beans gestionados por Spring
        UsuarioRepository usuarioRepository = context.getBean(UsuarioRepository.class);
        ClasePrueba otraClase = context.getBean(ClasePrueba.class);  // Obtener otra clase que ya tiene el repositorio
        PruebaServer otraServer = context.getBean(PruebaServer.class);

        // Usar el repositorio desde MainApp
        System.out.println("Referencia de UsuarioRepository en MainApp: " + usuarioRepository);
        otraClase.usarRepositorio();  // Llamar a un método de OtraClase que usa el repositorio
        otraServer.usarRepositorio();
        context.close();
    }
}
