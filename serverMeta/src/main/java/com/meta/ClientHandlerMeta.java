package com.meta;
import java.io.*;
import java.net.*;

public class ClientHandlerMeta extends Thread{
    private final Socket clientSocket;
    private final AuthServerMeta server;

    public ClientHandlerMeta(Socket clientSocket, AuthServerMeta server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request = in.readLine(); // Leer solicitud del cliente
            String[] parts = request.split(";");
            String action = parts[0];
            String response = "";

            switch (action) {
                case "LOGIN":
                    response = server.login(parts[1], parts[2]);
                    break;
                case "REGISTER":
                    response = server.registerUser(parts[1], parts[2], parts[3]);
                    break;
                case "VALIDATE":
                    response = String.valueOf(server.validateToken(parts[1]));
                    break;
                case "GETINFO":
                    response = server.getUserInfo(parts[1]);
                    break;
                case "LOGOUT":
                    response = server.logout(parts[1]);
                    break;
                case "GETUSERSTORE":
                    response = server.getUserStoreAsJson();
                    break;
                case "GETUSERINFOSTORE":
                    response = server.getUserInfoStoreAsJson();
                    break;
                case "GETTOKENSTORE":
                    response = server.getTokenStoreAsJson();
                    break;
                default:
                    response = "Invalid action";
            }

            out.println(response); // Enviar respuesta al cliente
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Cerrar conexión
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
