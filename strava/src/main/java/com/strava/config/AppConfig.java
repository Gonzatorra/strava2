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

    /*@Bean
    public Servidor servidor(ApplicationContextProvider contextProvider) throws RemoteException {
        return new Servidor();
    }*/

    /*@Bean
    public IRemoteFacade remoteFacade() throws RemoteException {
        return new RemoteFacade();
    }*/

}

//spring Data JPA crea el @Bean de UsuarioRepository. 