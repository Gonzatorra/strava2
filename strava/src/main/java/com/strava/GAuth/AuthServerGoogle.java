package com.strava.GAuth;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.strava.DTO.*;

public class AuthServerGoogle {

    public static RemoteAuthFacadeG facade;

    public AuthServerGoogle() {
        try {
            this.facade = new RemoteAuthFacadeG();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = null;
            try {
                registry = LocateRegistry.getRegistry(1100);
                registry.list();
                System.out.println("RMI registry already exists.");
            } catch (RemoteException e) {
                System.out.println("Creating new RMI registry.");
                registry = LocateRegistry.createRegistry(1100);
            }

            AuthServerGoogle servidor = new AuthServerGoogle();

            IRemoteAuthFacadeG stub = (IRemoteAuthFacadeG) UnicastRemoteObject.exportObject(servidor.facade, 0);
            registry.rebind("RemoteAuthFacadeG", stub);

            System.out.println("AuthServer is ready and waiting for connections...");





        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}