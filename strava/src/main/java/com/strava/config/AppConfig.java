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
@ComponentScan(basePackages = {"com.strava", "com.google.server"})
public class AppConfig {

    @Bean
    public Servidor servidor(UsuarioRepository usuarioRepository) throws RemoteException {
        return new Servidor(usuarioRepository);
    }

    @Bean
    public IRemoteFacade remoteFacade(UsuarioRepository usuarioRepository) throws RemoteException {
        return new RemoteFacade(usuarioRepository);
    }

}

//spring Data JPA crea el @Bean de UsuarioRepository. 