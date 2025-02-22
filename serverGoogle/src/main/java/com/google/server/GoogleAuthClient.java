package com.google.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final UsuarioRepository usuarioRepository;  //Inyeccion del repositorio

    public GoogleAuthClient(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public boolean registerUser(String username, String password, String email) {
        try {
            @SuppressWarnings("deprecation")
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

            //Guardar el usuario en el repositorio local
            if (responseCode == 201) {
                Usuario usuario = new Usuario(username, password, email);
                usuarioRepository.save(usuario);  //Guardamos el usuario en el repositorio
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
                    System.out.println("Error: " + responseCode + " - " + responseBuilder);
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
            //Login en Google
            @SuppressWarnings("deprecation")
			URL url = new URL(GOOGLE_SERVER_URL + "/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String params = "username=" + username + "&password=" + password;

            //Enviar los parametros al servidor
            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //Si el login es correcto, leer el cuerpo de la respuesta
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    //Parsear la respuesta JSON para obtener solo el token
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(response.toString());
                    String token = rootNode.get("token").asText(); //Extraer el valor del token
                    System.out.println("Login exitoso. Token recibido: " + token);
                    return token; //Devolver solo el token
                }
            } else {
                System.out.println("Login fallido. Código de respuesta: " + responseCode);
                return null;  //Devolver null si el login no es exitoso
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;  //Devolver null en caso de error
        }
    }

    public boolean logoutUser(String username) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL(GOOGLE_SERVER_URL + "/logout");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //Parametros de la solicitud
            String params = "username=" + username;

            //Enviar los parametros al servidor
            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //Si la respuesta es 200, logout fue exitoso
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

    public List<Usuario> allUsers() {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL(GOOGLE_SERVER_URL + "/all-users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);  //No es necesario enviar parámetros, así que no se necesita output stream

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                //Si la respuesta es 200, leer los usuarios de la respuesta
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    //Usamos Jackson para deserializar la respuesta JSON
                    ObjectMapper objectMapper = new ObjectMapper();

                    List<Usuario> usuarios = objectMapper.readValue(response.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Usuario.class));
                    System.out.println("Usuarios recibidos: " + response);
                    return usuarios; //Los usuarios se recibieron correctamente
                }
            } else {
                System.out.println("Error al obtener usuarios. Código de respuesta: " + responseCode);
                return null;  //Si la respuesta no es 200, se da un error
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;  //Si ocurre algun error, se maneja aquí
        }
    }

}
