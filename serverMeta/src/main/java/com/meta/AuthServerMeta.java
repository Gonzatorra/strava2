package com.meta;

import java.io.*;
import java.net.*;
import java.util.*;

public class AuthServerMeta {
    private static final int PORT = 1101; //Puerto del servidor
    private final Map<String, String> userStore = new HashMap<>();
    private final Map<String, String> tokenStore = new HashMap<>();
    private final Map<String, String> userInfoStore = new HashMap<>();

    public AuthServerMeta() {
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("AuthServer se ejecuta en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandlerMeta(clientSocket, this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String login(String username, String password) {
        if (userStore.containsKey(username) && userStore.get(username).equals(password)) {
            //String token = UUID.randomUUID().toString();
            String token = "meta_" + System.currentTimeMillis();
            tokenStore.put(token, username);
            return token;
        }
        return "Invalid credentials";
    }

    public synchronized String registerUser(String username, String password, String email) {
        if (userStore.containsKey(username)) {
            return "User already exists";
        }

        userStore.put(username, password);
        userInfoStore.put(username, username + ", " + email);
        return "User registered successfully";
    }

    public synchronized boolean validateToken(String token) {
        return tokenStore.containsKey(token);
    }

    public synchronized String getUserInfo(String token) {
        String username = tokenStore.get(token);
        if (username != null && userInfoStore.containsKey(username)) {
            return userInfoStore.get(username);
        }
        return "Invalid token";
    }

    public synchronized String logout(String token) {
        if (tokenStore.containsKey(token)) {
            tokenStore.remove(token);
            return ("Logout hecho para el token: " + token);
        } else {
            return("Token no valido para el logout: " + token);
        }

    }
    public String getUserStoreAsJson() {
        return mapToJson(userStore);
    }

    public String getUserInfoStoreAsJson() {
        return mapToJson(userInfoStore);
    }

    public String getTokenStoreAsJson() {
        return mapToJson(tokenStore);
    }

    private String mapToJson(Map<String, String> map) {
        StringBuilder jsonBuilder = new StringBuilder("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            jsonBuilder.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 1); //Elimina la ultima coma
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }


    public static void main(String[] args){
        new AuthServerMeta().start();
    }
}
