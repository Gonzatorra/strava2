package com.google.server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Optional;


@RestController
@RequestMapping("/google")
public class AuthControllerGoogle {
    private final UsuarioService usuarioService;

    public AuthControllerGoogle(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //Login: Recibir username y password, y devolver el usuario con el token si es correcto
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            Usuario usuario = usuarioService.login(username, password);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    //Logout: Recibir username y eliminar el token si existe
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("username") String username) {
        Optional<Usuario> usuario = usuarioService.logout(username);

        if (usuario.isPresent()) {
            return ResponseEntity.ok("Usuario deslogueado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado o ya está deslogueado.");
        }
    }

    //Registro: Recibir username, email y contraseña, y registrar al usuario
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password) {
        try {
            Usuario usuario = usuarioService.register(username, email, password);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());  //En caso de que el usuario ya exista
        }
    }


    //Validar usuario: Comprobar que el token es valido
    @PostMapping("/validate")
    public ResponseEntity<String> validateUser(@RequestParam("username") String username, @RequestParam("token") String token) {
        Optional<Usuario> usuario = usuarioService.findByUsername(username);

        if (usuario.isPresent()) {
            // Validar si el token es correcto
            if (usuario.get().getToken().equals(token)) {
                return ResponseEntity.ok("Token validado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }
    }

}
