package com.strava.rmi;

import com.google.server.GoogleAuthClient;
import com.google.server.ServerApplication;
import com.google.server.Usuario;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class StravaApplication {
  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(ServerApplication.class, args);
    GoogleAuthClient googleAuthClient = context.getBean(GoogleAuthClient.class);
    googleAuthClient.registerUser("K","J","J");
    googleAuthClient.allUsers();
  }
}