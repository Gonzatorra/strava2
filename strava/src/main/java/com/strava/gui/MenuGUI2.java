package com.strava.gui;

import com.strava.config.AppConfig;
import com.strava.config.ApplicationContextProvider;
import com.strava.fachada.IRemoteFacade;
import com.google.server.UsuarioRepository;
import com.strava.fachada.RemoteFacade;
import com.strava.rmi.Servidor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.rmi.Naming;
import java.util.Map;

public class MenuGUI2 {
    private final UsuarioRepository usuarioRepository;

    public MenuGUI2(UsuarioRepository usuarioRepository) {
        AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) ApplicationContextProvider.getContext();
        this.usuarioRepository = context.getBean(UsuarioRepository.class);
    }

    public static void main(String[] args) {
        /*try {
            AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) ApplicationContextProvider.getContext();
            UsuarioRepository usuarioRepository = context.getBean(UsuarioRepository.class);
            Servidor servidor = context.getBean(Servidor.class);
            RemoteFacade remoteFacade = context.getBean(RemoteFacade.class);
            IRemoteFacade facade = (IRemoteFacade) Naming.lookup("rmi://localhost/RemoteFacade");


            System.out.println("Accediendo al repositorio de usuarios GUI: " + usuarioRepository);


        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
