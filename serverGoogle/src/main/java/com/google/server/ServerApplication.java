package com.google.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServerApplication.class, args);
        GoogleAuthClient googleAuthClient = context.getBean(GoogleAuthClient.class);

        googleAuthClient.registerUser("daniel333", "claveDaniel", "daniel333@gmail.com");
        googleAuthClient.registerUser("susana555", "claveSusana", "susana555@gmail.com");
        googleAuthClient.registerUser("manuel111", "claveManuel", "manuel111@gmail.com");
        googleAuthClient.registerUser("isabel999", "claveIsabel", "isabel999@gmail.com");
        googleAuthClient.registerUser("andres444", "claveAndres", "andres444@gmail.com");
        googleAuthClient.registerUser("clara777", "claveClara", "clara777@gmail.com");
        googleAuthClient.registerUser("pablo888", "clavePablo", "pablo888@gmail.com");

        //String token = googleAuthClient.loginUser("daniel333", "claveDaniel");
        //System.out.println(token);
    }
}

