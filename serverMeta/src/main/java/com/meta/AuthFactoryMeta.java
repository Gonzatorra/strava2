package com.meta;
import java.util.*;

public class AuthFactoryMeta {
    public static String createToken(String username) {
        return UUID.randomUUID().toString();
    }

    public static void registerUser(
            String username,
            String password,
            String email,
            Map<String, String> userStore,
            Map<String, String> userInfoStore) {

        // Agrega el usuario al userStore
        userStore.put(username, password);

        // Agrega la información del usuario al userInfoStore
        userInfoStore.put(username, username + ", " + email);
    }

}
