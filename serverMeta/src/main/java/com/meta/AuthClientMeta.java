package com.meta;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class AuthClientMeta {
    private final String host;
    private final int port;

    public AuthClientMeta(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendRequest(String request) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(request);
            System.out.println(request);
            String token=in.readLine();
            return token;
            	//throw new NullPointerException();
            
        }
    }

    public String registerUser(String username, String password, String email) throws IOException {
        return sendRequest("REGISTER;" + username + ";" + password + ";" + email);
    }

    public String login(String username, String password) throws IOException {
        return sendRequest("LOGIN;" + username + ";" + password);
    }

    public boolean validateToken(String token) throws IOException {
        return Boolean.parseBoolean(sendRequest("VALIDATE;" + token));
    }

    public String getUserInfo(String token) throws IOException {
        return sendRequest("GETINFO;" + token);
    }

    public Map<String, String> getUserStore() throws IOException {
        String jsonResponse = sendRequest("GETUSERSTORE");
        return parseJsonToMap(jsonResponse);
    }

    public Map<String, String> getUserInfoStore() throws IOException {
        String jsonResponse = sendRequest("GETUSERINFOSTORE");
        return parseJsonToMap(jsonResponse);
    }

    public Map<String, String> getTokenStore() throws IOException {
        String jsonResponse = sendRequest("GETTOKENSTORE");
        return parseJsonToMap(jsonResponse);

    }

    private Map<String, String> parseJsonToMap(String json) {
        Gson gson = new Gson();
        // Convierte el JSON en un Map<String, String>
        return gson.fromJson(json, Map.class);
    }


    public void logout(String username) throws IOException {
        sendRequest("LOGOUT;" + username);
        System.out.println("Logout exitoso para el usuario: " + username);
    }
}
