package com.strava.auth;

public class MetaAuthServiceGateway implements IAuthServiceGateway {

    @Override
    public boolean autenticar(String username, String password, String token) {
        return token != null && token.startsWith("meta_") && !username.equals("") && !password.equals("");
    }

    @Override
    public String getProveedor() {
        return "Meta";
    }

    @Override
    public String generarToken() {
        return "meta_" + System.currentTimeMillis();
    }
}