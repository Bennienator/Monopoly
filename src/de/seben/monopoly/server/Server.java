package de.seben.monopoly.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private static Server instance;
    public static Server getInstance(){
        if(instance == null)
            instance = new Server();
        return instance;
    }
    private boolean running;

    private ServerSocket serverSocket;
    private ClientController controller;

    private Server(){}

    public void start(){
        if(!running){
            running = true;
            System.out.println("Servus! Ich bin ein Server.");
            try {
                serverSocket = new ServerSocket(7777);
            }catch (IOException e){
                e.printStackTrace();
            }
            this.controller = ClientController.getInstance().start();
        }
    }

    public ServerSocket getSocket(){
        return this.serverSocket;
    }

}
