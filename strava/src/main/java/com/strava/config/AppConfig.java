package com.strava.config;


import com.google.server.GoogleAuthClient;
import com.strava.rmi.Servidor;
import com.google.server.UsuarioRepository;
import com.strava.fachada.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.rmi.RemoteException;

@Configuration
@ComponentScan(basePackages = {"com.strava", "com.google.server"})  // Escanea el paquete com.strava para detectar @Component, @Service, etc.
public class AppConfig {

    @Bean
    public Servidor servidor() throws RemoteException {
        // Creamos el servidor, pasando el mismo usuarioRepository inyectado
        return new Servidor();
    }

    @Bean
    public IRemoteFacade remoteFacade(UsuarioRepository usuarioRepository) throws RemoteException {
        return new RemoteFacade(usuarioRepository);
    }

    @Bean
    public UsuarioRepository usuarioRepository() {
        return new UsuarioRepository();
    }

}
