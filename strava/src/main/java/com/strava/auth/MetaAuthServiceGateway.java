package com.strava.auth;

public class MetaAuthServiceGateway implements IAuthServiceGateway {

	@Override
    public boolean autenticar(String username, String password, String token) {
        return token != null && token.startsWith("meta_") && !username.equals("")  && !password.equals("");
    }


    @Override
    public String generarToken() {
        return "meta_" + System.currentTimeMillis();
    }

}


///Aquí he implementado la clase así:
/*

package com.strava.auth;

import com.meta.AuthClientMeta;
import java.io.IOException;

public class MetaAuthServiceGateway implements IAuthServiceGateway {

    private final AuthClientMeta metaClient;

    public MetaAuthServiceGateway(String host, int port) {
        this.metaClient = new AuthClientMeta(host, port);
    }

    @Override
    public boolean autenticar(String username, String password, String token) {
        return token != null && token.startsWith("meta_")
               && !username.isEmpty()
               && !password.isEmpty();
    }

    @Override
    public String generarToken() {
        return "meta_" + System.currentTimeMillis();
    }

    public void registerUser(String username, String password, String email) throws IOException {
        metaClient.registerUser(username, password, email);
    }

    public String login(String username, String password) throws IOException {
        return metaClient.login(username, password);
    }

    public void logout(String username) throws IOException {
        metaClient.sendRequest("LOGOUT;" + username);
    }

    public java.util.Map<String, String> getUserStore() {
        return metaClient.getUserStore();
    }

}


*/