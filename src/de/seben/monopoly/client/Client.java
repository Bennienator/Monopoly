package de.seben.monopoly.client;

import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ServerSocket serverSocket;

    public Client(){

        System.out.println("Servus! Ich bin ein Client.");

    }

}