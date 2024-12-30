package com.google.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService servicioAutenticacion;

    @GetMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("proveedor") String proveedor) {
        //Llamar al servicio de autenticacion
        String token = servicioAutenticacion.loginUser(username, password, proveedor);

        if (token != null) {
            return "Login exitoso. Token: " + token;
        } else {
            return "Credenciales incorrectas o usuario no encontrado.";
        }
    }

    @GetMapping("/logout")
    public String logout(@RequestParam("username") String username) {
        servicioAutenticacion.logout(username);
        return "Logout exitoso.";
    }
}
