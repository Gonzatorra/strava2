package com.meta;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface IRemoteAuthFacadeMeta extends Remote {
    String registerUser(String username, String password, String email) throws RemoteException;

    String login(String username, String password) throws RemoteException;

}
