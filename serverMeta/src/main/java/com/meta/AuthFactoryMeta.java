package com.meta;
import java.util.*;

public class AuthFactoryMeta {
    public static String createToken(String username) {
    	return "meta_" + System.currentTimeMillis();
    } //deberia ir comentado porque el token se deberia crear en otro sitio y de otra forma-nora

    public static void registerUser(
            String username,
            String password,
            String email,
            Map<String, String> userStore,
            Map<String, String> userInfoStore) {

        //Añade el usuario al userStore
        userStore.put(username, password);

        //Añade la informacion del usuario al userInfoStore
        userInfoStore.put(username, username + ", " + email);
    }

}
