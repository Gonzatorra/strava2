package com.google.server;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class GoogleAuthClient {

    private static final String GOOGLE_SERVER_URL = "http://localhost:8080/google";

    public boolean registerUser(String username, String password, String email) {
        try {
            URL url = new URL(GOOGLE_SERVER_URL + "/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String params = "username=" + username + "&password=" + password + "&email=" + email;
            OutputStream os = conn.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("Usuario " + username + " registrado correctamente en Google");
            return responseCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            URL url = new URL(GOOGLE_SERVER_URL + "/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Parámetros de la solicitud
            String params = "username=" + username + "&password=" + password;

            // Enviar los parametros al servidor
            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // Si el login es exitoso, leer el cuerpo de la respuesta (por ejemplo, el token)
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("Login exitoso. Token recibido: " + response.toString());
                    return true;
                }
            } else {
                System.out.println("Login fallido. Código de respuesta: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateUser(String username, String token) {
        try {
            URL url = new URL(GOOGLE_SERVER_URL + "/validate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Parámetros de la solicitud (username y token)
            String params = "username=" + username + "&token=" + token;

            // Enviar los parámetros al servidor
            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            return responseCode == 200; // Si la respuesta es 200, el token es válido
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean logoutUser(String username) {
        try {
            URL url = new URL(GOOGLE_SERVER_URL + "/logout");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Parámetros de la solicitud
            String params = "username=" + username;

            // Enviar los parámetros al servidor
            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // Si la respuesta es 200, logout fue exitoso
                System.out.println("Logout exitoso.");
                return true;
            } else {
                System.out.println("Error al hacer logout. Código de respuesta: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
