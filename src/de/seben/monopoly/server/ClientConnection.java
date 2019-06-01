package de.seben.monopoly.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnection extends Thread{

    private ServerSocket server;
    private Socket client;

    public ClientConnection(ServerSocket server){
        this.server = server;
    }

    public void run(){
        try {
            while (true) {
                Socket socket = server.accept();
                if(socket != null){
                    client = socket;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
