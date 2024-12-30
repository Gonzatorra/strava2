package com.meta;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class RemoteAuthFacadeMeta implements IRemoteAuthFacadeMeta {
        private static RemoteAuthFacadeMeta instance; //Singleton

        private final Map<String, String> userStore = new HashMap<>();
        private final Map<String, String> tokenStore = new HashMap<>();
        private final Map<String, String> userInfoStore = new HashMap<>();

    private RemoteAuthFacadeMeta() throws RemoteException {
            //usuarios de prueba
            userStore.put("user1", "password1");
            userInfoStore.put("user1", "User One, user1@example.com");

            userStore.put("user2", "password2");
            userInfoStore.put("user2", "User Two, user2@example.com");


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
            return "Invalid credentials";
        }

        @Override
        public String registerUser(String username, String password, String email) throws RemoteException {
            if (userStore.containsKey(username)) {
                return "User already exists";
            }

            // Usar AuthFactory para registrar el usuario
            AuthFactoryMeta.registerUser(username, password, email, userStore, userInfoStore);

            return "User registered successfully";
        }


        @Override
        public boolean validateToken(String token) throws RemoteException {
            return tokenStore.containsKey(token);
        }

        @Override
        public String getUserInfo(String token) throws RemoteException {
            String username = tokenStore.get(token);
            if (username != null && userInfoStore.containsKey(username)) {
                return userInfoStore.get(username);
            }
            return "Invalid token";
        }
}
