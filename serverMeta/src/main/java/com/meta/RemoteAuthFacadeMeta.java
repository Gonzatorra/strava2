package com.meta;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class RemoteAuthFacadeMeta implements IRemoteAuthFacadeMeta {
        private static RemoteAuthFacadeMeta instance; //Singleton

        private final Map<String, String> userStore = new HashMap<>();
        private final Map<String, String> tokenStore = new HashMap<>();
        private final Map<String, String> userInfoStore = new HashMap<>();

    private RemoteAuthFacadeMeta() throws RemoteException {
        }


		//Singleton
        public static RemoteAuthFacadeMeta getInstance() throws RemoteException {
            if (instance == null) {
                synchronized (RemoteAuthFacadeMeta.class) {
                    if (instance == null) {
                        instance = new RemoteAuthFacadeMeta();
                    }
                }
            }
            return instance;
        }

        @Override
        public String login(String username, String password) throws RemoteException {
            if (userStore.containsKey(username) && userStore.get(username).equals(password)) {
                String token = AuthFactoryMeta.createToken(username);
                tokenStore.put(token, username);
                return token;
            }
            return "Credenciales incorrectos";
        }

        @Override
        public String registerUser(String username, String password, String email) throws RemoteException {
            if (userStore.containsKey(username)) {
                return "El usuario ya existe";
            }

            //Usar AuthFactory para registrar el usuario
            AuthFactoryMeta.registerUser(username, password, email, userStore, userInfoStore);

            return "Usuario registrado correctamente";
        }
}
