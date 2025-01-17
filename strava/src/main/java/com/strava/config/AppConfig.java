package com.strava.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import java.rmi.RemoteException;

@Configuration
@ComponentScan(basePackages = {"com.strava", "com.google.server"})
public class AppConfig {

}

//spring Data JPA crea el @Bean de UsuarioRepository. 