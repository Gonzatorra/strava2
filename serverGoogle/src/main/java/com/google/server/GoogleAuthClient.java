package com.google.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.*;


@Component
public class GoogleAuthClient {

    private static final String GOOGLE_SERVER_URL = "http://localhost:8080/google";
    private final UsuarioRepository usuarioRepository;  // Inyección del repositorio

    public GoogleAuthClient(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public boolean registerUser(String username, String password, String email) {
        try {
            // Registro en Google
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

            // Guardar el usuario en el repositorio local
            if (responseCode == 201) {
                Usuario usuario = new Usuario(username, password, email);
                usuarioRepository.save(usuario);  // Guardamos el usuario en el repositorio
                System.out.println("Usuario " + username + " guardado en repositorio correctamente en Google");
                return true;
            }else{
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                    String line;
                    StringBuilder responseBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    System.out.println("Error: " + responseCode + " - " + responseBuilder.toString());
                } else {
                    System.out.println("Error: Código de respuesta " + responseCode);
                }
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String loginUser(String username, String password) {
        try {
            // Login en Google
            URL url = new URL(GOOGLE_SERVER_URL + "/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Parámetros de la solicitud
            String params = "username=" + username + "&password=" + password;

            // Enviar los parámetros al servidor
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
                    // Asumimos que la respuesta es el token
                    String token = response.toString();
                    System.out.println("Login exitoso. Token recibido: " + token);
                    return token;  // Retornar el token recibido
                }
            } else {
                System.out.println("Login fallido. Código de respuesta: " + responseCode);
                return null;  // Retornar null si el login no es exitoso
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Retornar null en caso de error
        }
    }

    //Metodo para obtener todos los usuarios registrados
    public Map<String, Usuario> getAllUsers() {
        return usuarioRepository.getUsuarios();  // Retorna todos los usuarios del repositorio
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
