package de.seben.monopoly.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {

    private ServerSocket serverSocket;

    public Server(){

        System.out.println("Servus! Ich bin ein Server.");
        try {
            serverSocket = new ServerSocket(7777);
        }catch (IOException e){
            e.printStackTrace();
        }

        for(int i = 0; i < 4; i++){
            ClientController.getInstance().createNewClientConnection(serverSocket);
        }

    }

    public void broadcastChatMessage(String message){
        //TODO: implement method
    }

    /*
    Wenn der CommandType 'EOR' vom aktiven Client gesendet wird, ist der nächste Spieler dran.
     */

}
