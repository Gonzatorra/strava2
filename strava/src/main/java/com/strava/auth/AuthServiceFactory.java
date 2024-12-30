package com.strava.auth;


public class AuthServiceFactory {
    public static IAuthServiceGateway getAuthService(String proveedor) {
        switch (proveedor) {
            case "Google":
                return new GoogleAuthServiceGateway();
            case "Meta":
                return new MetaAuthServiceGateway();
            case "Strava":
                return new StravaAuthServiceGateway();
            default:
                throw new IllegalArgumentException("Proveedor no soportado: " + proveedor);
        }
    }
}